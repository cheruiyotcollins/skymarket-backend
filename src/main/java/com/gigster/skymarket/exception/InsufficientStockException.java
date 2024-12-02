package com.gigster.skymarket.exception;

import lombok.Getter;

@Getter
public class InsufficientStockException extends RuntimeException {

    private String productName;
    private int requestedQuantity;
    private int availableStock;

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(String message, String productName, int requestedQuantity, int availableStock) {
        super(message);
        this.productName = productName;
        this.requestedQuantity = requestedQuantity;
        this.availableStock = availableStock;
    }

}
