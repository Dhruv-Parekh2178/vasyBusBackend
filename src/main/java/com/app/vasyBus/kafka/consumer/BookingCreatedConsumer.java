package com.app.vasyBus.kafka.consumer;

import com.app.vasyBus.kafka.event.BookingCreatedEvent;
import com.app.vasyBus.service.email.EmailService;
import com.app.vasyBus.service.email.EmailTemplateBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.app.vasyBus.config.KafkaTopicConfig.BOOKING_CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingCreatedConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = BOOKING_CREATED,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(BookingCreatedEvent event) {
        log.info("Consumed booking-created event for bookingId: {}", event.getBookingId());
        try {
            String subject = "VasyBus — Booking #" + event.getBookingId() + " Created (Payment Pending)";
            String body    = EmailTemplateBuilder.bookingCreated(event);
            emailService.sendHtmlEmail(event.getUserEmail(), subject, body);
        } catch (Exception e) {
            log.error("Failed to send booking-created email for bookingId {}: {}",
                    event.getBookingId(), e.getMessage());
        }
    }
}