package com.app.vasyBus.dto.route;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RouteRequestDTO {

    @NotBlank(message = "Source city is required")
    @Size(min = 2, max = 50,
          message = "Source city must be between 2 and 50 characters")
    @JsonProperty("source_city")
    private String sourceCity;

    @NotBlank(message = "Destination city is required")
    @Size(min = 2, max = 50,
          message = "Destination city must be between 2 and 50 characters")
    @JsonProperty("destination_city")
    private String destinationCity;

    @NotNull(message = "Distance is required")
    @Positive(message = "Distance must be greater than 0")
    @Max(value = 5000, message = "Distance looks unrealistic")
    @JsonProperty("distance_km")
    private Double distanceKm;

    @NotBlank(message = "Estimated time is required")
    @Pattern(
            regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$",
            message = "Estimated time must be in HH:mm format (Example: 08:30)"
    )
    @JsonProperty("estimated_time")
    private String estimatedTime;
}