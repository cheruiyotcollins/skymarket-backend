package com.gigster.skymarket.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDtoResponse {
    private String productName;
    private double price;
    private int quantity;
    private double subTotal;
}

