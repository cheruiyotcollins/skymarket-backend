package com.gigster.skymarket.dto;

import lombok.Data;

@Data
public class NewProductDto {
    private String title;
    private long category_id;
    private String manufacturer;
    private String description;
    private String image;
    private double price;
}