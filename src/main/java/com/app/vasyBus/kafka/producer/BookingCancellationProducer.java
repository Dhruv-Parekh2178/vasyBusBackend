package com.app.vasyBus.kafka.producer;

import com.app.vasyBus.config.KafkaTopicConfig;
import com.app.vasyBus.kafka.event.BookingCancelledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingCancellationProducer {

    private final KafkaTemplate<String, BookingCancelledEvent> kafkaTemplate;

    public void sendBookingCancelledEvent(BookingCancelledEvent event) {

        kafkaTemplate.send(
                KafkaTopicConfig.BOOKING_CANCELLED,
                event.getBookingId().toString(),
                event
        );

        log.info("Kafka Event Sent → booking-cancelled : {}", event.getBookingId());
    }
}