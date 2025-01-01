package com.gigster.skymarket.utils;

import com.gigster.skymarket.repository.UserRepository;

public class IdGeneratorUtil {

    private IdGeneratorUtil() {
        // Private constructor to prevent instantiation
    }

    public static Long generateUniqueCustomerId(UserRepository userRepository) {
        Long customerId;
        do {
            customerId = generateRandomCustomerId();
        } while (userRepository.existsByCustomerId(customerId));
        return customerId;
    }

    public static Long generateRandomCustomerId() {
        // Generates a random number between 1,000,000,000 and 9,999,999,999 (10-digit Long)
        return (long) (1_000_000_000L + (Math.random() * 9_000_000_000L));
    }
}
