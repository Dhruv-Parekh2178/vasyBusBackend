package com.app.vasyBus.dto.booking;

import com.app.vasyBus.dto.bookingSeat.BookingSeatRequestDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BookingRequestDTO {
    @NotNull(message = "Schedule ID is required")
    @JsonProperty("schedule_id")
    private Long scheduleId;

    @JsonProperty("seat_ids")
    @NotEmpty(message = "At least one seat must be selected")
    private List<Long> seatIds;

    @NotEmpty(message = "Passenger details are required")
    @Valid
    private List<BookingSeatRequestDTO> passengers;
}
