package com.app.vasyBus.dto.bus;

import com.app.vasyBus.enums.BusType;

import java.time.Instant;

public interface BusResponseDTO {

    Long getBusId();
    String getBusName();
    String getBusNumber();
    BusType getBusType();
    Integer getTotalSeats();
    String getOperatorName();
    Instant getCreatedAt();
}
