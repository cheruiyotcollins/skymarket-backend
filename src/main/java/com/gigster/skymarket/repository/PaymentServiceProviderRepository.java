package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.PaymentServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentServiceProviderRepository extends JpaRepository<PaymentServiceProvider, Long> {
    Optional<PaymentServiceProvider> findByServiceProviderName(String serviceProviderName);

    // Find by Short Code
    Optional<PaymentServiceProvider> findByShortCode(String shortCode);
}
