package com.gigster.skymarket.exception;

import lombok.Getter;

@Getter
public class OrderNotFoundException extends RuntimeException {

    private Long orderId;

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Long orderId) {
        super(message);
        this.orderId = orderId;
    }
}
