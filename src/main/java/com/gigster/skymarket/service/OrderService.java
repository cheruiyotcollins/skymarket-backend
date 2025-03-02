package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.OrderDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.model.Order;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity<ResponseDto> createOrder(OrderDto orderDto);

    ResponseEntity<ResponseDto> getOrderById(Long orderId);

    void saveOrder(Order order);

    // 3. Retrieve all orders, ADMIN.
    ResponseEntity<ResponseDto> getAllOrders(int page, int size, String sort);

    ResponseEntity<ResponseDto> updateOrder(Long orderId, OrderDto orderDto);

    ResponseEntity<ResponseDto> deleteOrder(Long orderId);

    ResponseEntity<ResponseDto> cancelOrder(Long orderId);
}