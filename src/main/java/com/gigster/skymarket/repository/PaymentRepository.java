package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Finds a payment by its payment reference.
     *
     * @param paymentReference the unique reference of the payment
     * @return the Payment object if found, null otherwise
     */
    Payment findByPaymentReference(String paymentReference);
}