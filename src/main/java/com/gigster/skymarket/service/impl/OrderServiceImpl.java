package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.OrderDto;
import com.gigster.skymarket.dto.OrderProductDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.enums.OrderStatus;
import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.model.Order;
import com.gigster.skymarket.model.OrderProduct;
import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.repository.CustomerRepository;
import com.gigster.skymarket.repository.OrderRepository;
import com.gigster.skymarket.repository.ProductRepository;
import com.gigster.skymarket.repository.UserRepository;
import com.gigster.skymarket.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ProductRepository productRepository;
    ResponseDto responseDto;
    @Autowired
    NotificationServiceImpl notificationService;
    @Autowired
    UserRepository userRepository;

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

        Order order = mapToOrder(orderDto, customer, products);
        order.setStatus(OrderStatus.PENDING);
        order = orderRepository.save(order);
        Order finalOrder = order;
        //Sending the email via notification service
        new Thread(new Runnable() {
            public void run() {
                String sendTo = customer.getEmail();
                String subject = "Order Confirmation";
                String emailBody = "Dear " + customer.getFullName() + "," + "\n\n" +
                        "Thank you for your recent order. We are pleased to confirm that your order has been received and is now being processed." + "\n" +
                        "Order Details:" + "\n" +
                        "Order Number: " + finalOrder.getOrderNumber() + "\n" +  // Assuming you have an order object
                        "Order Date: " + finalOrder.getOrderDate() + "\n" +
                        "Total Amount: KES " + finalOrder.getTotalAmount() + "\n\n" +
                        "We will notify you once your order has been shipped. You can track the status of your order by logging into your account." + "\n\n" +
                        "If you have any questions, please feel free to contact our customer service team." + "\n\n" +
                        "Regards," + "\n" +
                        "SkyMarket Online Store Team";


                notificationService.sendMail(sendTo, subject, emailBody);
            }
        }).start();

        return mapToOrderDto(order);
    }
    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
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
        responseDto = new ResponseDto();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found or could not be deleted"));

        orderRepository.delete(order);
            responseDto.setStatus(HttpStatus.ACCEPTED);
            responseDto.setDescription("Order deleted successfully");

            return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
   //cancelling orders(Customers,admins)
   @Override
    public ResponseEntity<ResponseDto> cancelOrder(Long orderId) {
        responseDto=new ResponseDto();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if(order.getStatus().equals(OrderStatus.PENDING) || order.getStatus().equals(OrderStatus.PROCESSING)){
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            responseDto.setStatus(HttpStatus.ACCEPTED);
            responseDto.setDescription("Order Cancelled Successfully");
            responseDto.setPayload(order);
        }else{
            responseDto.setStatus(HttpStatus.NOT_ACCEPTABLE);
            responseDto.setDescription("Orders currently being shipped or already delivered can not be cancelled");
        }
        return new ResponseEntity<>(responseDto,responseDto.getStatus());


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
