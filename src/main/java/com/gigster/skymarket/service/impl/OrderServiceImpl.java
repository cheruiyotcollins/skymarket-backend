package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.OrderDto;
import com.gigster.skymarket.dto.OrderProductDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.enums.OrderStatus;
import com.gigster.skymarket.model.*;
import com.gigster.skymarket.repository.*;
import com.gigster.skymarket.service.OrderService;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public OrderDto createOrder(OrderDto orderDto) {
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
        // todo check if ordered product is less or equal to product stock
        // todo concurrency/ threads locks when fetching product stock

        Order order = mapToOrder(orderDto, customer, products);
        order.setStatus(OrderStatus.PENDING);
        order = orderRepository.save(order);

        notificationService.sendOrderConfirmationEmail(customer, order);

        // Completely deleting the cart after a successful order.
        cartRepository.findByCustomerId(customer.getId())
                .ifPresent(cartRepository::delete);


        return mapToOrderDto(order);
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

    // 3. Retrieve a single order by ID
    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapToOrderDto(order);  // Map the order entity to OrderDto and return it
    }

    // 4. Update an existing order
    @Override
    public OrderDto updateOrder(Long orderId, OrderDto orderDto) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Fetch products based on IDs from the orderDto

        List<Long> productIds = orderDto.getOrderProducts()
                .stream()
                .map(OrderProductDto::getProductId) // Assuming OrderProductDto has a getProductId() method
                .collect(Collectors.toList());

// Now you can fetch the products using the product IDs
        List<Product> products = productRepository.findAllById(productIds);
        if (products.isEmpty()) {
            throw new RuntimeException("Products not found");
        }

        // Update order details
        existingOrder.setOrderNumber(orderDto.getOrderNumber());
        existingOrder.setTotalAmount(orderDto.getTotalAmount());
        existingOrder.setStatus(orderDto.getStatus());
        existingOrder.setOrderDate(orderDto.getOrderDate());
        existingOrder.setCustomer(customer);  // Update customer

        // Create OrderProduct instances
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (Product product : products) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(existingOrder);  // Set the current order
            orderProduct.setProduct(product);       // Set the product
            // Set a default quantity or get it from the orderDto if available
            orderProduct.setQuantity(1);  // Or use orderDto.getQuantity() if applicable
            orderProducts.add(orderProduct);
        }

        // Set the updated list of OrderProducts
        existingOrder.setOrderProducts(orderProducts);

        existingOrder = orderRepository.save(existingOrder);  // Update the order in the database

        return mapToOrderDto(existingOrder);  // Convert to DTO and return
    }


    // 5. Delete an order by ID (Admin)
    @Override
    public ResponseEntity<ResponseDto> deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found or could not be deleted"));

        orderRepository.delete(order);
        return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED, "Order deleted successfully ");

    }
   //cancelling orders(Customers,admins)
   @Override
    public ResponseEntity<ResponseDto> cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if(order.getStatus().equals(OrderStatus.PENDING) || order.getStatus().equals(OrderStatus.PROCESSING)){
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED, "Order Cancelled Successfully",order);

        }else{
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_ACCEPTABLE, "Orders currently being shipped or already delivered can not be cancelled");

        }
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
        orderDto.setOrderProducts(orderProductDtos);  // Set the list of OrderProductDtos

        return orderDto;
    }

}
