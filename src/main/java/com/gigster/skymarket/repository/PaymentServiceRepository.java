package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentServiceRepository extends JpaRepository<Payment, Long> {
}
