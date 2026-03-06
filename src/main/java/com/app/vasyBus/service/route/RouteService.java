package com.app.vasyBus.service.route;

import com.app.vasyBus.dto.route.RouteRequestDTO;
import com.app.vasyBus.dto.route.RouteResponseDTO;

import java.util.List;

public interface RouteService {
    RouteResponseDTO addRoute(RouteRequestDTO routeRequestDTO);

    List<RouteResponseDTO> getAllRoutes();

    RouteResponseDTO getRouteById(Long id);

    RouteResponseDTO updateRoute(Long id , RouteRequestDTO routeRequestDTO);

    String deleteRoute(Long id);

}
