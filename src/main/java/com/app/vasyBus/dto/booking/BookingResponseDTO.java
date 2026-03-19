package com.app.vasyBus.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public interface BookingResponseDTO {
    Long getBookingId();
    Long getUserId();
    String getUserName();
    Long getScheduleId();
    String getBusName();
    String getBusType();
    String getSourceCity();
    String getDestinationCity();
    @JsonFormat(pattern = "HH:mm")
    LocalTime getDepartureTime();
    @JsonFormat(pattern = "HH:mm")
    LocalTime getArrivalTime();
    LocalDate getTravelDate();
    BigDecimal getTotalAmount();
    String getBookingStatus();
    String getPaymentStatus();
    String getCancelledBy();
    String getCancellationReason();
    Instant getCreatedAt();
}