package com.bazarya.domain.entities;

public enum OrderStatus {
    NEW,
    CONFIRMED,
    PACKED,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    RTS,
    RETURN_REQUESTED,
    RETURNED,
    REFUNDED
}
