package com.app.vasyBus.dto.seat;

import java.time.Instant;

public interface SeatResponseDTO {
    Long getSeatId();
    Long getScheduleId();
    String getSeatNumber();
    String getSeatType();
    Boolean getBooked();
    String getSeatStatus();
    Instant getCreatedAt();
}
