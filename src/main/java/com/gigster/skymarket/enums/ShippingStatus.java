package com.gigster.skymarket.enums;

public enum ShippingStatus {
    PENDING,         // Shipment is created but not yet shipped
    PROCESSING,      // Shipment is being prepared (e.g., packaged, labeled)
    IN_TRANSIT,      // Shipment is on the way to the destination
    OUT_FOR_DELIVERY, // Shipment is out for final delivery
    DELIVERED,       // Shipment has been successfully delivered
    RETURNED,        // Shipment has been returned to the sender
    CANCELLED        // Shipment has been canceled
}
