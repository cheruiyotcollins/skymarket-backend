package com.gigster.skymarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {
    private String name;
    private Long cartId;
    private CustomerDto customerDto;
    private List<CartItemResponseDto> cartItemDtoList;
    private double totalPrice;
}