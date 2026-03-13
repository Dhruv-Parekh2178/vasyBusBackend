package com.app.vasyBus.service.payment;

import com.app.vasyBus.dto.payment.PaymentIntentResponse;
import com.app.vasyBus.dto.payment.PaymentRequestDTO;

public interface PaymentService {
    PaymentIntentResponse createPaymentIntent(PaymentRequestDTO paymentRequestDTO, Long userId);
    String handleWebhook(String payload, String sigHeader);
}