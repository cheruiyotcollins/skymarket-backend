package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.OrderDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.OrderService;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    //todo move this to service
    @Autowired
    ResponseDtoMapper responseDtoSetter;

    // 1. CREATE: Add a new order
    @PostMapping
    public ResponseEntity<ResponseDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        try {
            orderService.createOrder(orderDto);
            return responseDtoSetter.responseDtoSetter(HttpStatus.CREATED, "Order created successfully",orderDto);
        } catch (Exception e) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST, "Failed to create order+>>>"+e);
        }
    }

    // 2. READ: Get all orders
    @GetMapping
    public ResponseEntity<ResponseDto> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    // 3. READ: Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getOrderById(@PathVariable Long id) {
        try {
            OrderDto order = orderService.getOrderById(id);
            return responseDtoSetter.responseDtoSetter(HttpStatus.OK, "Order details",order);
        } catch (Exception e) {
            log.error("Error fetching order by id: {}", e.getMessage());
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "Order not found");
        }
    }

    // 4. UPDATE: Update an existing order
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDto orderDto) {
        try {
            OrderDto updatedOrder = orderService.updateOrder(id, orderDto);
            return responseDtoSetter.responseDtoSetter(HttpStatus.OK, "Order updated successfully",updatedOrder);
        } catch (Exception e) {
            log.error("Error updating order: {}", e.getMessage());
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST, "Failed to update order");
        }
    }

    // 5. DELETE: Delete an order by ID, admin
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteOrder(@PathVariable Long id) {
       return orderService.deleteOrder(id);

    }
    // use patch for partial update
    @PatchMapping("/id/{id}")
    public ResponseEntity<ResponseDto> cancelOrder(@PathVariable Long id){
        return orderService.cancelOrder(id);
    }
}
