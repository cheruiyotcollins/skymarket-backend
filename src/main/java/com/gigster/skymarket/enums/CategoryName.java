package com.gigster.skymarket.enums;

public enum CategoryName {
    ELECTRONICS,
    GROCERIES,
    FASHION,
    FURNITURE,
    STATIONARY,
    MENSCLOTHING,
    JEWELERY,
    WOMENSCLOTHING;
    public static CategoryName fromString(String categoryName) {
        return CategoryName.valueOf(categoryName.toUpperCase());
    }
}