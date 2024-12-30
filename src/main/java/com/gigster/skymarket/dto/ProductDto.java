package com.gigster.skymarket.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private String name;
    private String description;
    private Double price;
    private Long categoryId;
    private String imageUrl;
    private Long productId;

}
