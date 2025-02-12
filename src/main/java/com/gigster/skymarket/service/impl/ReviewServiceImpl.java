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

import com.gigster.skymarket.utils.DateUtils;
import java.time.Instant;
import java.util.HashSet;
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
    public boolean isVerifiedPurchase(Long userId, Long productId) {
        return orderRepository.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    public void addReview(Long userId, Long productId, int rating, String comment) {
        if (!isVerifiedPurchase(userId, productId)) {
            throw new UnauthorizedException("You must purchase the product to leave a review.");
        }

        boolean alreadyReviewed = reviewRepository.existsByUser_UserIdAndProductId(userId, productId);
        if (alreadyReviewed) {
            throw new UnauthorizedException("You have already reviewed this product.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Review review = new Review();
        review.setProductId(productId);
        review.setRating(rating);
        review.setComment(comment);
        review.setVerifiedPurchase(true);
        review.setUser(user);
        review.setCreatedAt(Instant.now());

        reviewRepository.save(review);

        updateProductRating(productId);
    }

    @Override
    public void addComment(Long userId, Long productId, String comment) {
        boolean alreadyReviewed = reviewRepository.existsByUser_UserIdAndProductId(userId, productId);
        if (alreadyReviewed) {
            throw new UnauthorizedException("You have already reviewed this product.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Review review = new Review();
        review.setProductId(productId);
        review.setComment(comment);
        review.setUser(user);
        review.setCreatedAt(Instant.now());

        reviewRepository.save(review);
    }

    @Override
    public void likeReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        // Check if the user has already liked the review
        if (review.getLikes().contains(userId)) {
            throw new UnauthorizedException("You have already liked this review.");
        }

        review.getLikes().add(userId); // Add the user ID to the likes set
        reviewRepository.save(review);
    }

    @Override
    public void dislikeReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        // Check if the user has already disliked the review
        if (review.getDislikes().contains(userId)) {
            throw new UnauthorizedException("You have already disliked this review.");
        }

        // Check if the user has liked the review, if so, remove the like
        review.getLikes().remove(userId);

        // Add the user ID to the dislikes set
        if (review.getDislikes() == null) {
            review.setDislikes(new HashSet<>());  // Initialize dislikes if it’s null
        }
        review.getDislikes().add(userId);  // Add the user ID to the dislikes set

        reviewRepository.save(review);
    }

    private void updateProductRating(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId)
                .stream()
                .filter(review -> review.getRating() > 0)
                .toList();

        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        int totalReviews = reviews.size();
        productRepository.updateProductRating(productId, averageRating, totalReviews);
    }

    @Override
    public List<ReviewDto> getReviewsForProduct(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(review -> new ReviewDto(
                        review.getId(),
                        review.getUser().getUsername(),
                        review.getRating(),
                        review.getLikes(),
                        review.getComment(),
                        review.isVerifiedPurchase(),
                        DateUtils.formatInstant(review.getCreatedAt())

                ))
                .collect(Collectors.toList());
    }

    @Override
    public void markAsVerified(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        review.setVerifiedPurchase(true);
        reviewRepository.save(review);
    }
}
