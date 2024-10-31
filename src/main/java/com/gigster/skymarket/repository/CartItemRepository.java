package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.CartItem;
import com.gigster.skymarket.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
