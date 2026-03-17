package com.app.vasyBus.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String BOOKING_CREATED   = "booking-created";
    public static final String PAYMENT_SUCCESS   = "payment-success";
    public static final String BOOKING_CANCELLED = "booking-cancelled";


    @Bean
    public NewTopic bookingCreatedTopic() {return TopicBuilder.name(BOOKING_CREATED).partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic paymentSuccessTopic() {return TopicBuilder.name(PAYMENT_SUCCESS).partitions(1)
                .replicas(1)
                .build();}

    @Bean
    public NewTopic bookingCancelledTopic() {return TopicBuilder.name(BOOKING_CANCELLED).partitions(1)
                .replicas(1)
                .build();}
}