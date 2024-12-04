package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.OrderDto;
import com.gigster.skymarket.dto.OrderProductDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.enums.OrderStatus;
import com.gigster.skymarket.exception.InsufficientStockException;
import com.gigster.skymarket.exception.OrderNotFoundException;
import com.gigster.skymarket.model.*;
import com.gigster.skymarket.repository.*;
import com.gigster.skymarket.service.OrderService;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationServiceImpl notificationService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ResponseDtoMapper responseDtoSetter;

    // 1. Create a new Order
    @Override
    @Transactional
    public ResponseEntity<ResponseDto> createOrder(OrderDto orderDto) {
        try {
            Customer customer = customerRepository.findById(orderDto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("We could not find your account. Please check your details or try again later."));

            List<Long> productIds = orderDto.getOrderProducts()
                    .stream()
                    .map(OrderProductDto::getProductId)
                    .collect(Collectors.toList());

            List<Product> products = productRepository.findAllByIdsWithLock(productIds);

            if (products.isEmpty()) {
                throw new RuntimeException("Some products in your cart are no longer available. Please review your order.");
            }

        // Stock validation - ensure each product has enough stock
            for (OrderProductDto orderProductDto : orderDto.getOrderProducts()) {
                Product product = products.stream()
                        .filter(p -> Long.valueOf(p.getId()).equals(orderProductDto.getProductId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("The product you selected is out of stock. Please try another or check back later."));

                if (orderProductDto.getQuantity() > product.getStock()) {
                    throw new RuntimeException("Sorry, we do not have enough stock for the product: " + product.getProductName() + ". Please adjust the quantity or choose a different product.");
                }
            }

            // Create order and set status
            Order order = mapToOrder(orderDto, customer, products);
            order.setStatus(OrderStatus.PENDING_PAYMENT);

            // Concurrency control using pessimistic locking
            order = orderRepository.save(order);

            // Deduct stock after the order is saved
            for (OrderProductDto orderProductDto : orderDto.getOrderProducts()) {
                Product product = productRepository.findByIdWithLock(orderProductDto.getProductId())
                        .orElseThrow(() -> new InsufficientStockException("The product you selected is no longer available. Please review your order."));

                // Update the stock by deducting the ordered quantity
                int updatedStock = product.getStock() - orderProductDto.getQuantity();
                if (updatedStock < 0) {
                    throw new InsufficientStockException("We don’t have enough stock for: " + product.getProductName() + ". Please choose a different quantity or product.");
                }

                product.setStock(updatedStock);
                productRepository.save(product);
            }

            // Send order confirmation email
            notificationService.sendOrderConfirmationEmail(customer, order);

            // Clean up the cart after successful order
            cartRepository.findByCustomerId(customer.getId())
                    .ifPresent(cartRepository::delete);

            ResponseDto response = ResponseDto.builder()
                    .status(HttpStatus.CREATED)
                    .description("Your order has been successfully created.")
                    .payload(mapToOrderDto(order))
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            ResponseDto errorResponse = ResponseDto.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .description("Failed to process your order: " + e.getMessage())
                    .payload(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // 3. Retrieve a single order by ID
    @Override
    public ResponseEntity<ResponseDto> getOrderById(Long orderId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            OrderDto orderDto = mapToOrderDto(order);

            ResponseDto response = ResponseDto.builder()
                    .status(HttpStatus.OK)
                    .description("Order details")
                    .payload(orderDto)
                    .build();

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            ResponseDto errorResponse = ResponseDto.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .description(e.getMessage())
                    .payload(null)
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getAllOrders(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderDto> orderDtos = orderPage.getContent()
                .stream()
                .map(this::mapToOrderDto)
                .toList();

        ResponseDto responseDto = responseDtoSetter.responseDtoSetter(
                HttpStatus.OK,
                "Fetched List of All Orders.",
                orderDtos
        ).getBody();

        assert responseDto != null;
        responseDto.setTotalPages(orderPage.getTotalPages());
        responseDto.setTotalElements(orderPage.getTotalElements());
        responseDto.setCurrentPage(pageable.getPageNumber());
        responseDto.setPageSize(pageable.getPageSize());

        return ResponseEntity.ok(responseDto);
    }

    // 4. Update an existing order
    @Override
    public ResponseEntity<ResponseDto> updateOrder(Long orderId, OrderDto orderDto) {
        try {
            Order existingOrder = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            Customer customer = customerRepository.findById(orderDto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            List<Long> productIds = orderDto.getOrderProducts()
                    .stream()
                    .map(OrderProductDto::getProductId)
                    .collect(Collectors.toList());

            List<Product> products = productRepository.findAllById(productIds);
            if (products.isEmpty()) {
                throw new RuntimeException("Products not found");
            }

            // Update order details
            existingOrder.setOrderNumber(orderDto.getOrderNumber());
            existingOrder.setTotalAmount(orderDto.getTotalAmount());
            existingOrder.setStatus(orderDto.getStatus());
            existingOrder.setOrderDate(orderDto.getOrderDate());
            existingOrder.setCustomer(customer);

            // Create OrderProduct instances
            List<OrderProduct> orderProducts = new ArrayList<>();
            for (Product product : products) {
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setOrder(existingOrder);
                orderProduct.setProduct(product);
                orderProduct.setQuantity(1);
                orderProducts.add(orderProduct);
            }

            existingOrder.setOrderProducts(orderProducts);

            existingOrder = orderRepository.save(existingOrder);

            OrderDto updatedOrderDto = mapToOrderDto(existingOrder);

            ResponseDto response = ResponseDto.builder()
                    .status(HttpStatus.OK)
                    .description("Order updated successfully")
                    .payload(updatedOrderDto)
                    .build();

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // In case of errors, return a ResponseDto with the error message
            ResponseDto errorResponse = ResponseDto.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .description(e.getMessage())
                    .payload(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // 5. Delete an order by ID (Admin)
    @Override
    public ResponseEntity<ResponseDto> deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found or could not be deleted"));

        orderRepository.delete(order);
        return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED, "Order deleted successfully ");

    }

    // Cancelling orders (Customers, admins)
    @Override
    public ResponseEntity<ResponseDto> cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        // Check if the order is in a cancellable state
        if (isCancellable(order.getStatus())) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            return responseDtoSetter.responseDtoSetter(
                    HttpStatus.ACCEPTED,
                    "Order cancelled successfully.",
                    order
            );
        }

        // Handle invalid cancellation attempt
        return responseDtoSetter.responseDtoSetter(
                HttpStatus.NOT_ACCEPTABLE,
                "Orders currently being shipped or already delivered cannot be cancelled."
        );
    }

    // Method to determine if an order can be cancelled.
    private boolean isCancellable(OrderStatus status) {
        return EnumSet.of(
                OrderStatus.PENDING_PAYMENT,
                OrderStatus.PAYMENT_CONFIRMED,
                OrderStatus.PROCESSING
        ).contains(status);
    }

    // Mapping OrderDto to Order
    private Order mapToOrder(OrderDto orderDto, Customer customer, List<Product> products) {
        Order order = new Order();
        order.setId(orderDto.getOrderId());
        order.setOrderNumber(orderDto.getOrderNumber());
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setStatus(orderDto.getStatus());
        order.setOrderDate(orderDto.getOrderDate());
        order.setCustomer(customer);

        // Create OrderProduct instances for each product in the order
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderProductDto orderProductDto : orderDto.getOrderProducts()) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);  // Set the current order

            // Fetch the product from the database
            Product product = productRepository.findById(orderProductDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            orderProduct.setProduct(product);  // Set the fetched product
            orderProduct.setQuantity(orderProductDto.getQuantity());  // Set quantity

            orderProducts.add(orderProduct);
        }

        order.setOrderProducts(orderProducts);  // Set the list of OrderProducts
        return order;
    }

    // Mapping Order to OrderDto
    private OrderDto mapToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getId());
        orderDto.setOrderNumber(order.getOrderNumber());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setStatus(order.getStatus());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setCustomerId(order.getCustomer().getId());

        // Map OrderProducts to OrderProductDto
        List<OrderProductDto> orderProductDtos = order.getOrderProducts()
                .stream()
                .map(orderProduct -> {
                    OrderProductDto dto = new OrderProductDto();
                    dto.setProductId(orderProduct.getProduct().getId());
                    dto.setQuantity(orderProduct.getQuantity());
                    return dto;
                })
                .collect(Collectors.toList());
        orderDto.setOrderProducts(orderProductDtos);

        return orderDto;
    }

}
