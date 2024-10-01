package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
