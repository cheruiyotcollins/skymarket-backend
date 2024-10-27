package com.gigster.skymarket.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private Long productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
}

