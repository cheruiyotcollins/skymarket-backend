package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.NewOrderDto;
import com.gigster.skymarket.dto.OrderDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.repository.CartRepository;
import com.gigster.skymarket.security.UserPrincipal;
import com.gigster.skymarket.service.OrderService;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    ResponseDtoMapper responseDtoSetter;
    @Autowired
    CartRepository cartRepository;

    private UserPrincipal getCurrentUser() {
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // 1. CREATE: Add a new order
    @PostMapping
    public ResponseEntity<ResponseDto> createOrder(@Valid @RequestBody NewOrderDto newOrderDto) {
        UserPrincipal userPrincipal = getCurrentUser();
        Long customerId=userPrincipal.getCustomerId();
        OrderDto orderDto= new OrderDto();
        orderDto.setCustomerId(customerId);
        Optional<Cart> cart;
            cart= cartRepository.findByCustomerId(customerId);

        orderDto.setCartId(cart.get().getCartId());
        orderDto.setPaymentMethod(newOrderDto.getPaymentMethod());
        orderDto.setShippingAddress(newOrderDto.getShippingAddress());
        return orderService.createOrder(orderDto);
    }

    // 3. READ: Get order by ID, admin and customer
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
    @GetMapping("/customer")
    public ResponseEntity<ResponseDto> getAllOrdersByCustomer() {
        UserPrincipal userPrincipal= getCurrentUser();
        Long customerId=userPrincipal.getCustomerId();
        return orderService.getAllOrdersByCustomer(customerId);
    }
    // 2. READ: Get all orders, admin
    @GetMapping
    public ResponseEntity<ResponseDto> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    // 4. UPDATE: Update an existing order
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDto orderDto) {
        return orderService.updateOrder(id, orderDto);
    }

    // 5. PATCH: Cancel an order
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    // 6. DELETE: Delete an order by ID, admin
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteOrder(@PathVariable Long id) {
       return orderService.deleteOrder(id);

    }

}
