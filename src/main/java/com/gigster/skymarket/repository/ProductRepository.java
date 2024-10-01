package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Product;
import com.gigster.skymarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
