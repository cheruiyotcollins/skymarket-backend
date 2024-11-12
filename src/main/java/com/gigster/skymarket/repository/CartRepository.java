package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomerId(Long customer);
}
