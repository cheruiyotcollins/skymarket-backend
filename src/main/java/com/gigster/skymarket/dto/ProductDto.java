package com.gigster.skymarket.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private long id;
    private String title;
    private String description;
    private Double price;
    private String category;
    private String image;
    private Rating  rating;

}
