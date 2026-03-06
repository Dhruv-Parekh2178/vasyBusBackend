package com.app.vasyBus.dto.bus;

import com.app.vasyBus.enums.BusType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusRequestDTO {

    @NotBlank(message = "Bus name can't be null")
    @Size(min = 2, max = 30,
          message = "Bus name length must be between 2 and 30")
    @JsonProperty("bus_name")
    private String busName;

    @NotBlank(message = "Bus number is required")
    @Size(min = 5, max = 15,
          message = "Bus number must be between 5 and 15 characters")
    @JsonProperty("bus_number")
    private String busNumber;

    @NotNull(message = "Bus type is required")
    @JsonProperty("bus_type")
    private BusType busType;

    @NotNull(message = "Total seats is required")
    @Min(value = 25, message = "Bus must have minimum 25 seats")
    @Max(value = 100, message = "Maximum 100 seats allowed")
    @JsonProperty("total_seats")
    private Integer totalSeats;

    @NotBlank(message = "Operator name can't be null")
    @Size(min = 2, max = 30,
          message = "Operator name length must be between 2 and 30")
    @JsonProperty("operator_name")
    private String operatorName;
}