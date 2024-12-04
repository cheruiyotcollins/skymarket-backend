package com.gigster.skymarket.enums;

public enum RoleName {
    ADMIN,
    CUSTOMER;
    // Example getter for the enum name as a string
    public String getRoleName() {
        return this.name();
    }

    // Static method to get RoleName by name (case-insensitive)
    public static RoleName fromString(String roleName) {
        return RoleName.valueOf(roleName.toUpperCase());
    }
}
