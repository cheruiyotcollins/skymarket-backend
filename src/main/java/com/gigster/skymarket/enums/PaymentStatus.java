package com.gigster.skymarket.enums;

public enum PaymentStatus {
    PENDING,        // Payment has been initiated but not yet processed.
    COMPLETED,      // Payment was successful.
    FAILED,         // Payment failed due to an error.
    CANCELLED,      // Payment was cancelled by the user or system.
    REFUNDED,       // Payment was refunded to the customer.
    PARTIALLY_REFUNDED, // Only part of the payment was refunded.
    AUTHORIZED,     // Payment has been authorized but not captured.
    CAPTURED,       // Payment has been captured after authorization.
    EXPIRED,        // Payment authorization expired.
    CHARGEBACK,     // Payment is being disputed by the customer (chargeback process).
    PROCESSING,     // Payment is being processed by the gateway.
    REVERSED        // Payment was reversed (e.g., due to an error).
}
