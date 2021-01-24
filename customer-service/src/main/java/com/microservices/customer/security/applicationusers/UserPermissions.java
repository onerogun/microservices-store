package com.microservices.customer.security.applicationusers;

public enum UserPermissions {
    USER_UPDATE("user:update"),
    ITEM_UPDATE("item:update");

    private final String permission;


    private UserPermissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
