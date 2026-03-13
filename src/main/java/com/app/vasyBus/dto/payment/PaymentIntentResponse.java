package com.app.vasyBus.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentIntentResponse {
    private String clientSecret;
    private String paymentIntentId;
    private Long bookingId;
    private Long amountInPaise;
    private String currency;
}