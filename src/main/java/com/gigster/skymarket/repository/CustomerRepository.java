package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
