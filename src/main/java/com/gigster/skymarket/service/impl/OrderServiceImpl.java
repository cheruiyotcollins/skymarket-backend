package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.OrderDto;
import com.gigster.skymarket.dto.OrderProductDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.enums.OrderStatus;
import com.gigster.skymarket.exception.OrderNotFoundException;
import com.gigster.skymarket.model.*;
import com.gigster.skymarket.repository.*;
import com.gigster.skymarket.service.NotificationService;
import com.gigster.skymarket.service.OrderService;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
    private NotificationService notificationService;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    ResponseDtoMapper responseDtoSetter;

    // 1. Create a new Order.
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(
            retryFor = PessimisticLockingFailureException.class,
            backoff = @Backoff(delay = 100)
    )

    public ResponseEntity<ResponseDto> createOrder(OrderDto orderDto) {
        try {
            // Validate customer
            Customer customer = customerRepository.findById(orderDto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("We could not find your account. Please check your details or try again later."));

            // Validate the cart after validating the customer.
            Cart cart = cartRepository.findById(orderDto.getCartId())
                    .orElseThrow(() -> new RuntimeException("We could not find the cart you are trying to make order with."));

            // Additional validation; ensuring the cart belongs to the customer.
            if (!cart.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
                throw new RuntimeException("This cart does not belong to the authenticated customer.");
            }

            // Extract product IDs and validate inputs
            List<OrderProductDto> orderProducts = cart.getCartItems()
                    .stream()
                    .map(cartItem -> new OrderProductDto(cartItem.getProduct().getId(), cartItem.getQuantity())) // Create OrderProductDto
                    .toList();
            orderDto.setOrderProducts(orderProducts);
            List<Long> productIds = orderProducts
                    .stream()
                    .map(OrderProductDto::getProductId)
                    .collect(Collectors.toList());

            if (productIds.isEmpty()) {
                throw new RuntimeException("Your cart is empty. Please add products before placing an order.");
            }

            // Lock products and validate stock
            List<Product> products = productRepository.findAllByIdsWithLock(productIds);
           if (products.isEmpty()) {
                throw new RuntimeException("Some products in your cart are no longer available. Please review your order.");
            }

            Map<Long, Integer> stockValidationMap = products.stream()
                    .collect(Collectors.toMap(Product::getId, Product::getStock));
            for (OrderProductDto orderProductDto : orderProducts) {
                Integer availableStock = stockValidationMap.get(orderProductDto.getProductId());
                if (availableStock == null || orderProductDto.getQuantity() > availableStock) {
                    throw new RuntimeException("Insufficient stock for product ID: " + orderProductDto.getProductId());
                }
            }

            // Bulk stock update
            for (OrderProductDto orderProductDto : orderProducts) {
                int updatedRows = productRepository.updateStockIfAvailable(
                        orderProductDto.getProductId(),
                        orderProductDto.getQuantity()
                );
                if (updatedRows == 0) {
                    throw new RuntimeException("Failed to update stock for product ID: " + orderProductDto.getProductId());
                }
            }
            orderDto.setOrderNumber(generateOrderNumber());
            orderDto.setStatus(OrderStatus.PENDING_PAYMENT);
            orderDto.setOrderDate(LocalDateTime.now());
            orderDto.setCreatedOn(LocalDateTime.now().toString());
            orderDto.setTotalAmount(cart.getTotalPrice());

            // Create order and persist it
            Order order = mapToOrder(orderDto, customer);
            order = orderRepository.save(order);
            // Clean up cart
           //todo make sure to find a way to delete cart items first before deleting cart
            cartRepository.delete(cart);
//            cartRepository.findByCustomerId(customer.getCustomerId())
//                    .ifPresent(cartRepository::delete);
            //todo
            // Notify customer asynchronously
            notificationService.sendOrderConfirmationEmail(customer, order);
            // Build and return success response
            ResponseDto response = ResponseDto.builder()
                    .status(HttpStatus.CREATED)
                    .description("Order created successfully.")
                    .payload(mapToOrderDto(order))
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            // Log and return error response
            log.error("Error creating order: {}", e.getMessage(), e);
            ResponseDto errorResponse = ResponseDto.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .description("Failed to create order: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // 2. Retrieve a single order by ID
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

    // 3. Retrieve all orders, ADMIN.
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

    // 4. Update an existing order.
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

    // 6. Cancelling orders (Customers, admins)
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

    // 7.
    @Override
    public void saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("Order saved successfully")
                .payload(savedOrder)
                .build());
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
    private Order mapToOrder(OrderDto orderDto, Customer customer) {
        Order order = new Order();
        order.setOrderNumber(orderDto.getOrderNumber());
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setStatus(orderDto.getStatus());
        order.setOrderDate(orderDto.getOrderDate());
        order.setCustomer(customer);
        order.setShippingAddress(orderDto.getShippingAddress());

//
//        // Create OrderProduct instances for each product in the order
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderProductDto orderProductDto : orderDto.getOrderProducts()) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);  // Set the current order

//            // Fetch the product from the database
            Product product = productRepository.findById(orderProductDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            orderProduct.setProduct(product);  // Set the fetched product
            orderProduct.setQuantity(orderProductDto.getQuantity());  // Set quantity

            orderProducts.add(orderProduct);
        }

         order.setOrderProducts(orderProducts);
        return order; // Set the list of OrderProducts

    }

    // Mapping Order to OrderDto
    private OrderDto mapToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getId());
        orderDto.setOrderNumber(order.getOrderNumber());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setStatus(order.getStatus());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setCustomerId(order.getCustomer().getCustomerId());

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
    private String generateOrderNumber() {
        long timestamp = Instant.now().toEpochMilli(); // Current time in milliseconds
        int randomPart = new Random().nextInt(900) + 100; // Generates a 3-digit random number (100-999)
        return "ORD-" + timestamp + "-" + randomPart; // Example: ORD-1709823745632-245
    }

}