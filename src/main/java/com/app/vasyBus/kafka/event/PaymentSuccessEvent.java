package com.app.vasyBus.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessEvent {

    private Long   bookingId;
    private Long   userId;
    private String userEmail;
    private String userName;

    private String stripePaymentId;
    private BigDecimal amountPaid;
    private String currency;

    private String busName;
    private String busType;
    private String sourceCity;
    private String destinationCity;
    private LocalDate travelDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    private List<BookingCreatedEvent.PassengerInfo> passengers;
}