package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Cart;
import com.gigster.skymarket.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomer(Customer customer);
}
