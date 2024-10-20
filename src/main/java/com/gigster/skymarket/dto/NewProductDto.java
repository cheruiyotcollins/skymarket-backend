package com.gigster.skymarket.dto;

import lombok.Data;

@Data
public class NewProductDto {
    private String productName;
    private String category;
    private String manufacturer;
    private double price;
    private int stock;
}
