package com.app.vasyBus.kafka.producer;

import com.app.vasyBus.config.KafkaTopicConfig;
import com.app.vasyBus.kafka.event.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventProducer {

    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    public void sendBookingCreatedEvent(BookingCreatedEvent event) {

        kafkaTemplate.send(
                KafkaTopicConfig.BOOKING_CREATED,
                event.getBookingId().toString(),
                event
        );

        log.info("Kafka Event Sent → booking-created : {}", event.getBookingId());
    }
}