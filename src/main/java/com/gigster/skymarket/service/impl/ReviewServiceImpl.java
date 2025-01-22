package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.ReviewDto;
import com.gigster.skymarket.model.Review;
import com.gigster.skymarket.exception.ResourceNotFoundException;
import com.gigster.skymarket.exception.UnauthorizedException;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.repository.OrderRepository;
import com.gigster.skymarket.repository.ReviewRepository;
import com.gigster.skymarket.repository.ProductRepository;
import com.gigster.skymarket.repository.UserRepository;
import com.gigster.skymarket.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    // Check if a user has purchased a product
    public boolean isVerifiedPurchase(Long userId, Long productId) {
        return orderRepository.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    // Add a new review
    public void addReview(Long userId, Long productId, int rating, String comment) {
        // Restrict reviews to verified purchasers
        if (!isVerifiedPurchase(userId, productId)) {
            throw new UnauthorizedException("You must purchase the product to leave a review.");
        }

        // Create and save the review
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Review review = new Review();
        review.setProductId(productId);
        review.setRating(rating);
        review.setComment(comment);
        review.setVerifiedPurchase(true);
        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());

        // Save the review to the database
        reviewRepository.save(review);

        // Update the product's average rating
        updateProductRating(productId);
    }

    // Update the average rating for a product
    private void updateProductRating(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        int totalReviews = reviews.size();

        // Update the product entity
        productRepository.updateProductRating(productId, averageRating, totalReviews);
    }

    @Override
    // Get all reviews for a product
    public List<ReviewDto> getReviewsForProduct(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);

        return reviews.stream()
                .map(review -> new ReviewDto(
                        review.getId(),
                        review.getUser().getUsername(),
                        review.getRating(),
                        review.getComment(),
                        review.isVerifiedPurchase(),
                        review.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    // Admin override: Mark a review as verified
    public void markAsVerified(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        review.setVerifiedPurchase(true);
        reviewRepository.save(review);
    }
}
