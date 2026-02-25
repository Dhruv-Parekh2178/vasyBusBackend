package com.app.vasyBus.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RouteRequestDTO {

    @NotBlank(message = "Source city is required")
    @Size(min = 2, max = 50,
          message = "Source city must be between 2 and 50 characters")
    private String sourceCity;

    @NotBlank(message = "Destination city is required")
    @Size(min = 2, max = 50,
          message = "Destination city must be between 2 and 50 characters")
    private String destinationCity;

    @NotNull(message = "Distance is required")
    @Positive(message = "Distance must be greater than 0")
    @Max(value = 5000, message = "Distance looks unrealistic")
    private Double distanceKm;

    @NotBlank(message = "Estimated time is required")
    @Pattern(
            regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$",
            message = "Estimated time must be in HH:mm format (Example: 08:30)"
    )
    private String estimatedTime;
}