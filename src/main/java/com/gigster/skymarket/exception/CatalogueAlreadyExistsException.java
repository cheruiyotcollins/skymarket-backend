package com.gigster.skymarket.exception;

public class CatalogueAlreadyExistsException extends RuntimeException {

    public CatalogueAlreadyExistsException(String message) {
        super(message);
    }

    public CatalogueAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
