package com.gigster.skymarket.dto;

import com.gigster.skymarket.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long orderId;
    private String orderNumber;
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private Long customerId;
    @Setter
    private List<OrderProductDto> orderProducts;
}
