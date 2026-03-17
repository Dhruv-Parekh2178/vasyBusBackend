package com.app.vasyBus.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreatedEvent {

    private Long   bookingId;
    private Long   userId;
    private String userEmail;
    private String userName;

    private String busName;
    private String busType;
    private String sourceCity;
    private String destinationCity;
    private LocalDate travelDate;
    private Instant departureTime;
    private Instant arrivalTime;

    private BigDecimal totalAmount;
    private List<PassengerInfo> passengers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerInfo {
        private String passengerName;
        private Integer passengerAge;
        private String passengerGender;
        private String seatNumber;
        private String seatType;
    }
}