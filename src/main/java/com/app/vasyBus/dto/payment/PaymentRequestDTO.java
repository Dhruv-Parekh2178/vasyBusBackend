package com.app.vasyBus.dto.payment;

import com.app.vasyBus.enums.CurrencyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {

    @NotNull(message = "Booking ID is required")
    @JsonProperty("booking_id")
    private Long bookingId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    @JsonProperty("amount")
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    @JsonProperty("currency")
    private CurrencyType currency;
}