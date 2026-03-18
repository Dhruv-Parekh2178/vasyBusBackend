package com.app.vasyBus.kafka.consumer;

import com.app.vasyBus.kafka.event.BookingCancelledEvent;
import com.app.vasyBus.service.email.EmailService;
import com.app.vasyBus.service.email.EmailTemplateBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.app.vasyBus.config.KafkaTopicConfig.BOOKING_CANCELLED;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingCancelledConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = BOOKING_CANCELLED,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(BookingCancelledEvent event) {
        log.info("Consumed booking-cancelled event for bookingId: {}", event.getBookingId());
        try {
            String subject = "VasyBus — Booking #" + event.getBookingId() + " Cancelled";
            String body    = EmailTemplateBuilder.bookingCancelled(event);
            emailService.sendHtmlEmail(event.getUserEmail(), subject, body);
        } catch (Exception e) {
            log.error("Failed to send booking-cancelled email for bookingId {}: {}",
                    event.getBookingId(), e.getMessage());
        }
    }
}