package com.app.vasyBus.kafka.producer;

import com.app.vasyBus.config.KafkaTopicConfig;
import com.app.vasyBus.kafka.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentSuccessEvent> kafkaTemplate;

    public void sendPaymentSuccessEvent(PaymentSuccessEvent event) {

        kafkaTemplate.send(
                KafkaTopicConfig.PAYMENT_SUCCESS,
                event.getBookingId().toString(),
                event
        );

        log.info("Kafka Event Sent → payment-success : {}", event.getBookingId());
    }
}