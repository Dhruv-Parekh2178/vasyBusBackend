package com.app.vasyBus.dto.schedule;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public interface ScheduleSearchDTO {
    Long getScheduleId();
    String getBusName();
    String getBusType();
    String getOperatorName();
    String getSourceCity();
    String getDestinationCity();
    Instant getDepartureTime();
    Instant getArrivalTime();
    LocalDate getTravelDate();
    BigDecimal getPricePerSeat();
    Integer getAvailableSeats();
    String getStatus();
}
