package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.PaymentServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentServiceProviderRepository extends JpaRepository<PaymentServiceProvider, Long> {
}
