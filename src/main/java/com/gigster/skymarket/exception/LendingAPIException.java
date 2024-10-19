package com.gigster.skymarket.exception;

import org.springframework.http.HttpStatus;

public class LendingAPIException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public LendingAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public LendingAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
