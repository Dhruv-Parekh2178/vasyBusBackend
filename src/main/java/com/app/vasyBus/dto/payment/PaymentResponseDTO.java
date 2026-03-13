package com.app.vasyBus.dto.payment;

public interface PaymentResponseDTO {
    Long getPaymentId();
    Long getBookingId();
    String getStripePaymentId();
    String getAmount();
    String getCurrency();
    String getPaymentStatus();
    String getCreatedAt();
}