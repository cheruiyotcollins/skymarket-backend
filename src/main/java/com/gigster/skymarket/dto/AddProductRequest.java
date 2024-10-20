package com.gigster.skymarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProductRequest {
    private String productName;
    private String category;
    private String manufacturer;
    private double price;
    private int stock;
}
