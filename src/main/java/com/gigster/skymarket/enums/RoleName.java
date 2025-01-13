package com.gigster.skymarket.enums;

public enum RoleName {
    ROLE_ADMIN,
    ROLE_CUSTOMER,
    ROLE_SUPERADMIN;

    // Example getter for the enum name as a string
    public String getRoleName() {
        return this.name();
    }

    // Static method to get RoleName by name (case-insensitive)
    public static RoleName fromString(String roleName) {
        try {
            return RoleName.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No enum constant for role: " + roleName, e);
        }
    }
}
