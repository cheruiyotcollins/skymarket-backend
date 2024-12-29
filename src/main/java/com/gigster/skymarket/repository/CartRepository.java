package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.customer.customerId = :customerId")
    Optional<Cart> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT COUNT(c) > 0 FROM Cart c WHERE c.customer.customerId = :customerId")
    boolean existsByCustomerId(@Param("customerId") Long customerId);
}
