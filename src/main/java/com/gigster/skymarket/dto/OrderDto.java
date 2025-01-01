package com.gigster.skymarket.dto;

import com.gigster.skymarket.enums.OrderStatus;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long orderId;
    private String orderNumber;
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private Long customerId;
    private Long cartId;
    @Setter
    private List<OrderProductDto> orderProducts;
    @Temporal(TemporalType.TIMESTAMP)
    private String CreatedOn;
}
