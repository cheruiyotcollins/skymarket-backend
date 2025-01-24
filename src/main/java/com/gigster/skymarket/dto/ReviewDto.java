package com.gigster.skymarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long id;
    private String username; // Username of the reviewer
    private int rating;
    private String comment;
    private boolean verifiedPurchase;
    private LocalDateTime createdAt;


}
