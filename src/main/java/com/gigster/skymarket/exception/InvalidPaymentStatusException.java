package com.gigster.skymarket.exception;

public class InvalidPaymentStatusException extends RuntimeException {
    public InvalidPaymentStatusException(String message) {
        super(message);
    }

    public InvalidPaymentStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
