package com.app.vasyBus.dto.seat;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeatLockRequestDTO {
    @NotNull(message = "seat Id is required")
    @JsonProperty("seat_id")
    private Long seatId;

    @NotNull(message = "schedule id is required")
    @JsonProperty("schedule_id")
    private Long scheduleId;

}
