package com.gigster.skymarket.enums;

public enum PaymentProvider {
    STRIPE,
    PAYPAL,
    RAZORPAY,
    MANUAL_BANK_TRANSFER  // For offline payments.
}
