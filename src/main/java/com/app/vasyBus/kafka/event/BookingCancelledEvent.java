package com.app.vasyBus.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCancelledEvent {

    private Long   bookingId;
    private Long   userId;
    private String userEmail;
    private String userName;

    private String busName;
    private String sourceCity;
    private String destinationCity;
    private LocalDate travelDate;
    private LocalTime departureTime;

    private BigDecimal totalAmount;
    private String cancelledBy;
    private String cancellationReason;
}