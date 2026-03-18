package com.app.vasyBus.kafka.consumer;

import com.app.vasyBus.kafka.event.PaymentSuccessEvent;
import com.app.vasyBus.service.email.EmailService;
import com.app.vasyBus.service.email.EmailTemplateBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.app.vasyBus.config.KafkaTopicConfig.PAYMENT_SUCCESS;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentSuccessConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = PAYMENT_SUCCESS,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(PaymentSuccessEvent event) {
        log.info("Consumed payment-success event for bookingId: {}", event.getBookingId());
        try {
            String subject = "VasyBus — Booking #" + event.getBookingId() + " Confirmed";
            String body    = EmailTemplateBuilder.paymentSuccess(event);
            emailService.sendHtmlEmail(event.getUserEmail(), subject, body);
        } catch (Exception e) {
            log.error("Failed to send payment-success email for bookingId {}: {}",
                    event.getBookingId(), e.getMessage());
        }
    }
}