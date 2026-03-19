package com.app.vasyBus.dto.schedule;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public interface ScheduleSearchDTO {
    Long getScheduleId();
    String getBusName();
    String getBusType();
    String getOperatorName();
    String getSourceCity();
    String getDestinationCity();
    LocalTime getDepartureTime();
    LocalTime getArrivalTime();
    LocalDate getTravelDate();
    BigDecimal getPricePerSeat();
    Integer getAvailableSeats();
    String getStatus();
}
