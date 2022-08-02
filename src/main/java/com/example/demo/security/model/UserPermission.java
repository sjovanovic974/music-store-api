package com.example.demo.security.model;

public enum UserPermission {
    PRODUCT_READ("product:read"),
    PRODUCT_WRITE("product:write"),
    CATEGORY_READ("category:read"),
    CATEGORY_WRITE("category:write"),
    ARTIST_READ("artist:read"),
    ARTIST_WRITE("artist:write");

    private final String permission;


    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
