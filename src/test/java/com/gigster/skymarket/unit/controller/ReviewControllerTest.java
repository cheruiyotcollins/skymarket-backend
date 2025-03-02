package com.gigster.skymarket.unit.controller;

import com.gigster.skymarket.controller.ReviewController;
import com.gigster.skymarket.dto.ReviewDto;
import com.gigster.skymarket.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private static final String SUCCESS_REVIEW_ADDED = "Review added successfully.";
    private static final String SUCCESS_COMMENT_ADDED = "Comment added successfully.";
    private static final String SUCCESS_REVIEW_LIKED = "Review liked successfully.";
    private static final String SUCCESS_REVIEW_DISLIKED = "Review disliked successfully.";
    private static final String SUCCESS_REVIEW_VERIFIED = "Review marked as verified.";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    void addReview_ShouldReturnOk() throws Exception {
        Long userId = 1L;
        Long productId = 2L;
        int rating = 5;
        String comment = "Great product!";

        mockMvc.perform(post("/api/v1/reviews/{productId}", productId)
                        .param("userId", userId.toString())
                        .param("rating", String.valueOf(rating))
                        .param("comment", comment)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(SUCCESS_REVIEW_ADDED));

        verify(reviewService, times(1)).addReview(userId, productId, rating, comment);
    }

    @Test
    void addComment_ShouldReturnOk() throws Exception {
        Long userId = 1L;
        Long productId = 2L;
        String comment = "Nice product!";

        mockMvc.perform(post("/api/v1/reviews/{productId}/comment", productId)
                        .param("userId", userId.toString())
                        .param("comment", comment)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(SUCCESS_COMMENT_ADDED));

        verify(reviewService, times(1)).addComment(userId, productId, comment);
    }

    @Test
    void likeReview_ShouldReturnOk() throws Exception {
        Long userId = 1L;
        Long reviewId = 10L;

        mockMvc.perform(patch("/api/v1/reviews/{reviewId}/like", reviewId)
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(SUCCESS_REVIEW_LIKED));

        verify(reviewService, times(1)).likeReview(userId, reviewId);
    }

    @Test
    void dislikeReview_ShouldReturnOk() throws Exception {
        Long userId = 1L;
        Long reviewId = 10L;

        mockMvc.perform(patch("/api/v1/reviews/{reviewId}/dislike", reviewId)
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(SUCCESS_REVIEW_DISLIKED));

        verify(reviewService, times(1)).dislikeReview(userId, reviewId);
    }

    @Test
    void getReviewsForProduct_ShouldReturnListOfReviews() throws Exception {
        Long productId = 2L;
        List<ReviewDto> reviews = List.of(
                new ReviewDto(1L, "User A", 5, new HashSet<>(), "Excellent!", true, "2024-02-04")
        );

        when(reviewService.getReviewsForProduct(productId)).thenReturn(reviews);

        mockMvc.perform(get("/api/v1/reviews/{productId}/reviews", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[0].comment").value("Excellent!"));

        verify(reviewService, times(1)).getReviewsForProduct(productId);
    }

    @Test
    void markAsVerified_ShouldReturnOk() throws Exception {
        Long reviewId = 5L;

        mockMvc.perform(patch("/api/v1/reviews/{reviewId}/verify", reviewId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(SUCCESS_REVIEW_VERIFIED));

        verify(reviewService, times(1)).markAsVerified(reviewId);
    }

    @Test
    void isVerifiedPurchase_ShouldReturnBoolean() throws Exception {
        Long userId = 1L;
        Long productId = 2L;

        when(reviewService.isVerifiedPurchase(userId, productId)).thenReturn(true);

        mockMvc.perform(get("/api/v1/reviews/verify/{productId}", productId)
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(reviewService, times(1)).isVerifiedPurchase(userId, productId);
    }
}
