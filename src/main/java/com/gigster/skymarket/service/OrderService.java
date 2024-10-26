package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.OrderDto;
import com.gigster.skymarket.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);

    List<OrderDto> getAllOrders();

    OrderDto getOrderById(Long orderId);

    OrderDto updateOrder(Long orderId, OrderDto orderDto);

    ResponseEntity<ResponseDto> deleteOrder(Long orderId);

    ResponseEntity<ResponseDto> cancelOrder(Long orderId);
}
