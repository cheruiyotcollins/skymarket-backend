package com.gigster.skymarket.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductRequestDto {
    private String productName;
    private String category;
    private String manufacturer;
    private double price;
    private int stock;
}
