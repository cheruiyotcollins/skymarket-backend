package com.gigster.skymarket.exception;

import lombok.Getter;

@Getter
public class ShippingNotFoundException extends RuntimeException {

    private Long shippingId;

    public ShippingNotFoundException(String message) {
        super(message);
    }

    public ShippingNotFoundException(String message, Long shippingId) {
        super(message);
        this.shippingId = shippingId;
    }
}
