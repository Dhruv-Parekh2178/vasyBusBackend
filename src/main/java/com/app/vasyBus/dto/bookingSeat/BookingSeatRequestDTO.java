package com.app.vasyBus.dto.bookingSeat;

import com.app.vasyBus.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookingSeatRequestDTO {

    @NotNull(message = "Seat ID is required")
    @JsonProperty("seat_id")
    private Long seatId;

    @NotBlank(message = "Passenger name can't be null")
    @Size(min = 2, max = 30,
          message = "Passenger name length must be between 2 and 30")
    @JsonProperty("passenger_name")
    private String passengerName;

    @NotNull(message = "Passenger age is required")
    @Min(value = 4, message = "Passenger minimum age should be 4 years")
    @JsonProperty("passenger_age")
    private Integer passengerAge;

    @NotNull(message = "Passenger gender is required")
    @JsonProperty("passenger_gender")
    private Gender passengerGender;
}