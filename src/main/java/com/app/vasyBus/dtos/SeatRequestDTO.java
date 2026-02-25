package com.app.vasyBus.dtos;

import com.app.vasyBus.model.enums.SeatType;
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