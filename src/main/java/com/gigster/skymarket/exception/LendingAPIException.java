package com.gigster.skymarket.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class LendingAPIException extends RuntimeException {

    @Getter
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

    @Override
    public String getMessage() {
        return message;
    }
}
