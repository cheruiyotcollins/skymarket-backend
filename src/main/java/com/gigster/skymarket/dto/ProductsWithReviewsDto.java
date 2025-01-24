package com.gigster.skymarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsWithReviewsDto {

    private Long productId;
    private String productName;
    private String description;
    private double averageRating;
    private int totalReviews;
    private List<ReviewDto> reviews;

}
