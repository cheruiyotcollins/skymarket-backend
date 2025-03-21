package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);

    boolean existsByUser_UserIdAndProductId(Long userId, Long productId);

    long countByUser_UserIdAndProductId(Long userId, Long productId);


}
