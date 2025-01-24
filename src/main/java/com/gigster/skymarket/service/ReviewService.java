package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.ReviewDto;

import java.util.List;

public interface ReviewService {

    boolean isVerifiedPurchase(Long userId, Long productId);

    void addReview(Long userId, Long productId, int rating, String comment);

    List<ReviewDto> getReviewsForProduct(Long productId);

    void markAsVerified(Long reviewId);
}
