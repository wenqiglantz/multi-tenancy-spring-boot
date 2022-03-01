package com.github.wenqiglantz.service.customerservice.data;

public enum CustomerStatus {
    CREATED,
    UPDATED,
    DELETED;

    public static CustomerStatus fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }
}
