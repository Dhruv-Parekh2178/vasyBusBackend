package com.app.vasyBus.dto.booking;

import com.app.vasyBus.dto.bookingSeat.BookingSeatRequestDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BookingRequestDTO {
    @NotNull(message = "User ID is required")
    @JsonProperty("user_id")
    private Long userId;

    @NotNull(message = "Schedule ID is required")
    @JsonProperty("schedule_id")
    private Long scheduleId;

    @JsonProperty("seat_ids")
    @NotNull(message = "Seat IDs are required")
    private List<BookingSeatRequestDTO> seatIds;

    @JsonProperty("total_amount")
    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;
}
