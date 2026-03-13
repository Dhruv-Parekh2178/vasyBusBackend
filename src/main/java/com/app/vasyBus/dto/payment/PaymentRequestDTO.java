package com.app.vasyBus.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequestDTO {

    @NotNull(message = "Booking ID is required")
    @JsonProperty("booking_id")
    private Long bookingId;
}   