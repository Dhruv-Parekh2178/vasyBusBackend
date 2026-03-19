package com.app.vasyBus.dto.schedule;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public interface ScheduleResponseDTO {
    Long getScheduleId();
    Long getBusId();
    String getBusName();
    String getBusType();
    Long getRouteId();
    String getSourceCity();
    String getDestinationCity();
    LocalTime getDepartureTime();
    LocalTime getArrivalTime();
    LocalDate getTravelDate();
    BigDecimal getPricePerSeat();
    String getScheduleStatus();
    Instant getCreatedAt();
}
