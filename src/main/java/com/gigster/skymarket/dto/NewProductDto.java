package com.gigster.skymarket.dto;

import lombok.Data;

@Data
public class NewProductDto {
    private String productName;
    private long category_id;
    private String manufacturer;
    private double price;
    private int stock;
}
