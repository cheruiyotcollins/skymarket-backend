package com.gigster.skymarket.enums;

import com.gigster.skymarket.model.Category;

public enum CategoryName {
    ELECTRONICS,
    GROCERIES,
    CLOTHING,
    FURNITURE,
    STATIONARY;
    public static CategoryName fromString(String categoryName) {
        return CategoryName.valueOf(categoryName.toUpperCase());
    }
}