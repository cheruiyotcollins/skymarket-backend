package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.OrderDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private ResponseDto responseDto;

    // 1. CREATE: Add a new order
    @PostMapping
    public ResponseEntity<ResponseDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        responseDto = new ResponseDto();
        try {
            orderService.createOrder(orderDto);
            responseDto.setStatus(HttpStatus.CREATED);
            responseDto.setDescription("Order created successfully");
            responseDto.setPayload(orderDto);
        } catch (Exception e) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("Failed to create order");
            log.error("Error creating order: {}", e.getMessage());
        }
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    // 2. READ: Get all orders
    @GetMapping
    public ResponseEntity<ResponseDto> getAllOrders() {
        responseDto = new ResponseDto();
        List<OrderDto> orders = orderService.getAllOrders();
        responseDto.setStatus(HttpStatus.OK);
        if (!orders.isEmpty()) {
            responseDto.setDescription("List of all orders");
            responseDto.setPayload(orders);
        } else {
            responseDto.setDescription("No orders found");
        }
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    // 3. READ: Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getOrderById(@PathVariable Long id) {
        responseDto = new ResponseDto();
        try {
            OrderDto order = orderService.getOrderById(id);
            responseDto.setStatus(HttpStatus.OK);
            responseDto.setDescription("Order details");
            responseDto.setPayload(order);
        } catch (Exception e) {
            responseDto.setStatus(HttpStatus.NOT_FOUND);
            responseDto.setDescription("Order not found");
            log.error("Error fetching order by id: {}", e.getMessage());
        }
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    // 4. UPDATE: Update an existing order
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDto orderDto) {
        responseDto = new ResponseDto();
        try {
            OrderDto updatedOrder = orderService.updateOrder(id, orderDto);
            responseDto.setStatus(HttpStatus.OK);
            responseDto.setDescription("Order updated successfully");
            responseDto.setPayload(updatedOrder);
        } catch (Exception e) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("Failed to update order");
            log.error("Error updating order: {}", e.getMessage());
        }
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    // 5. DELETE: Delete an order by ID, admin
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteOrder(@PathVariable Long id) {
        responseDto = new ResponseDto();
        try {
            orderService.deleteOrder(id);
            responseDto.setStatus(HttpStatus.OK);
            responseDto.setDescription("Order deleted successfully");
        } catch (Exception e) {
            responseDto.setStatus(HttpStatus.NOT_FOUND);
            responseDto.setDescription("Order not found or could not be deleted");
            log.error("Error deleting order: {}", e.getMessage());
        }
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
    // use patch for partial update
    @PatchMapping("/id/{id}")
    public ResponseEntity<ResponseDto> cancelOrder(@PathVariable Long id){
        return orderService.cancelOrder(id);
    }
}
