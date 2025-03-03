package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.OrderDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.service.OrderService;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    ResponseDtoMapper responseDtoSetter;

    // 1. CREATE: Add a new order
    @PostMapping
    public ResponseEntity<ResponseDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        return orderService.createOrder(orderDto);
    }

    // 3. READ: Get order by ID, admin
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // 2. READ: Get all orders, admin
    @GetMapping
    public ResponseEntity<ResponseDto> getAllOrders(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort) {

        return orderService.getAllOrders(page, size, sort);
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
