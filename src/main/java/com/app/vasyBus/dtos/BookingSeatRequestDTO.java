package com.app.vasyBus.dtos;

import com.app.vasyBus.model.enums.Gender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookingSeatRequestDTO {

    @NotNull(message = "Seat ID is required")
    private Long seatId;

    @NotBlank(message = "Passenger name can't be null")
    @Size(min = 2, max = 30,
          message = "Passenger name length must be between 2 and 30")
    private String passengerName;

    @NotNull(message = "Passenger age is required")
    @Min(value = 4, message = "Passenger minimum age should be 4 years")
    private Integer passengerAge;

    @NotNull(message = "Passenger gender is required")
    private Gender passengerGender;
}