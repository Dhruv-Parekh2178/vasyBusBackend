package com.app.vasyBus.dto.route;

import lombok.Data;


public interface RouteResponseDTO {
     Long getRouteId();
     String getSourceCity();
     String getDestinationCity();
     Double getDistanceKm();
     String getEstimatedTime();
}
