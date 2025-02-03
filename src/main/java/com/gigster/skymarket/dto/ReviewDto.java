package com.gigster.skymarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long id;
    private String username; // Username of the reviewer
    private int rating;
    private Set<Long> likes; // Store user IDs who liked the review
    private String comment;
    private boolean verifiedPurchase;
    private String createdAt;

}
