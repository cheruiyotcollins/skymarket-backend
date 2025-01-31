package com.gigster.skymarket.service;

import com.gigster.skymarket.enums.PaymentProvider;

import java.math.BigDecimal;

public interface PaymentService {
    boolean processPayment(String paymentReference, BigDecimal amount, String currency, String paymentMethod, String orderId, String customerId, PaymentProvider provider);

    void confirmPayment(String paymentReference);

    void handlePaymentFailure(String paymentReference, String failureReason);
}
