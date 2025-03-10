package com.gigster.skymarket.controller;

import com.gigster.skymarket.dto.ReviewDto;
import com.gigster.skymarket.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<?> addReview(@RequestParam Long userId, @PathVariable Long productId,
                                       @RequestParam int rating, @RequestParam String comment) {
        reviewService.addReview(userId, productId, rating, comment);
        return ResponseEntity.ok("Review added successfully");
    }

    @PostMapping("/{productId}/comment")
    public ResponseEntity<?> addComment(@RequestParam Long userId, @PathVariable Long productId,
                                        @RequestParam String comment) {
        reviewService.addComment(userId, productId, comment);
        return ResponseEntity.ok("Comment added successfully");
    }

    @PatchMapping("/{reviewId}/like")
    public ResponseEntity<?> likeReview(@RequestParam Long userId, @PathVariable Long reviewId) {
        reviewService.likeReview(userId, reviewId);
        return ResponseEntity.ok("Review liked successfully");
    }

    @PatchMapping("/{reviewId}/dislike")
    public ResponseEntity<?> dislikeReview(@RequestParam Long userId, @PathVariable Long reviewId) {
        reviewService.dislikeReview(userId, reviewId);
        return ResponseEntity.ok("Review disliked successfully");
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviewsForProduct(@PathVariable Long productId) {
        List<ReviewDto> reviews = reviewService.getReviewsForProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    @PatchMapping("/{reviewId}/verify")
    public ResponseEntity<?> markAsVerified(@PathVariable Long reviewId) {
        reviewService.markAsVerified(reviewId);
        return ResponseEntity.ok("Review marked as verified");
    }

    @GetMapping("/verify/{productId}")
    public ResponseEntity<Boolean> isVerifiedPurchase(@RequestParam Long userId, @PathVariable Long productId) {
        boolean isVerified = reviewService.isVerifiedPurchase(userId, productId);
        return ResponseEntity.ok(isVerified);
    }
}
