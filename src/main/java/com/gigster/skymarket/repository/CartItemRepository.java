package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCart_CartIdAndProductId(Long cartId, Long productId);

    Page<CartItem> findByCart_CartId(Long cartId, Pageable pageable);
    Optional<CartItem> findByCart_CartIdAndId(Long cartId, Long cartItemId);

    Optional<CartItem> findByProductId(Long productId);
}
