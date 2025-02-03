package com.gigster.skymarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private int rating;
    private String comment;
    private boolean verifiedPurchase;
    private Instant createdAt;

    @ElementCollection // Hibernate will create a separate table for storing likes
    private Set<Long> likes = new HashSet<>(); // Store user IDs who liked the review
    private Set<Long> dislikes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

