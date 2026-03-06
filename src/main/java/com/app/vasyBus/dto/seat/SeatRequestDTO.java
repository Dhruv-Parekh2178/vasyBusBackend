package com.app.vasyBus.dto.seat;

import com.app.vasyBus.enums.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeatRequestDTO {

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "Seat type is required")
    private SeatType seatType;
}