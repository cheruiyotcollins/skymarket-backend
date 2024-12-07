package com.gigster.skymarket.exception;

public class CustomerNotFoundException extends RuntimeException {

    // Constructor with a custom message
    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerNotFoundException() {
        super("Customer not found.");
    }
}
