package com.app.vasyBus.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BookingRequestDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Schedule ID is required")
    private Long scheduleId;

    @NotNull(message = "Seat IDs are required")
    private List<BookingSeatRequestDTO> seatIds;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;
}
