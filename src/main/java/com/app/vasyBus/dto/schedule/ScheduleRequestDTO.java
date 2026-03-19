package com.app.vasyBus.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleRequestDTO {

    @NotNull(message = "Bus ID is required")
    @JsonProperty("bus_id")
    private Long busId;

    @NotNull(message = "Route ID is required")
    @JsonProperty("route_id")
    private Long routeId;


    @NotNull(message = "Departure time is required")
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("departure_time")
    private LocalTime departureTime;

    @NotNull(message = "Arrival time is required")
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("arrival_time")
    private LocalTime arrivalTime;


    @NotNull(message = "Travel date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("travel_date")
    private LocalDate travelDate;

    @NotNull(message = "Price per seat is required")
    @Positive(message = "Price must be greater than 0")
    @JsonProperty("price_per_seat")
    private BigDecimal pricePerSeat;
}