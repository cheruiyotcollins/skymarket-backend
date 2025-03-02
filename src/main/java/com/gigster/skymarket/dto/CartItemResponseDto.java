package com.gigster.skymarket.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDto {
    private long id;
    private String title;
    private double price;
    private int quantity;
    private double subTotal;
}

