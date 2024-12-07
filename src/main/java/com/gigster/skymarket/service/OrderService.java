package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.OrderDto;
import com.gigster.skymarket.dto.ResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity<ResponseDto> createOrder(OrderDto orderDto);

    ResponseEntity<ResponseDto> getAllOrders(Pageable pageable);

    ResponseEntity<ResponseDto> getOrderById(Long orderId);

    ResponseEntity<ResponseDto> updateOrder(Long orderId, OrderDto orderDto);

    ResponseEntity<ResponseDto> deleteOrder(Long orderId);

    ResponseEntity<ResponseDto> cancelOrder(Long orderId);
}