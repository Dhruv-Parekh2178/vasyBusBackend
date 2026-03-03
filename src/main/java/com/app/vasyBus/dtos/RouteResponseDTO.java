package com.app.vasyBus.dtos;

import lombok.Data;

@Data
public class RouteResponseDTO {
    private String sourceCity;
    private String destinationCity;
    private Double distanceKm;
    private String estimatedTime;
}
