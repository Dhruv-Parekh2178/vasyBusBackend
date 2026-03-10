package com.app.vasyBus.dto.booking;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public interface BookingResponseDTO {
    Long getBookingId();
    Long getUserId();
    String getUserName();
    Long getScheduleId();
    String getBusName();
    String getBusType();
    String getSourceCity();
    String getDestinationCity();
    Instant getDepartureTime();
    Instant getArrivalTime();
    LocalDate getTravelDate();
    BigDecimal getTotalAmount();
    String getBookingStatus();
    String getPaymentStatus();
    String getCancelledBy();
    String getCancellationReason();
    Instant getCreatedAt();
}