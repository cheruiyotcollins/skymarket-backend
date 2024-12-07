package com.gigster.skymarket.utils;

import com.gigster.skymarket.exception.CustomException;

public class ExceptionAssert {

    private ExceptionAssert() {
        // Private constructor to prevent instantiation
    }

    public static void throwException(boolean condition, String message) {
        if (condition) {
            throw new CustomException(message);
        }
    }

    public static void throwException(String message) {
        throw new CustomException(message);
    }
}
