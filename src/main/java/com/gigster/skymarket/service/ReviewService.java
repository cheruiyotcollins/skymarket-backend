package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.ReviewDto;

import java.util.List;

public interface ReviewService {

    boolean isVerifiedPurchase(Long userId, Long productId);

    void addReview(Long userId, Long productId, int rating, String comment);

    void addComment(Long userId, Long productId, String comment);

    void likeReview(Long userId, Long reviewId);

    void dislikeReview(Long userId, Long reviewId);

    List<ReviewDto> getReviewsForProduct(Long productId);

    void markAsVerified(Long reviewId);
}
