package com.app.vasyBus.service.route;

import com.app.vasyBus.dto.route.RouteRequestDTO;
import com.app.vasyBus.dto.route.RouteResponseDTO;
import com.app.vasyBus.exception.ResourceNotFoundException;
import com.app.vasyBus.model.Route;
import com.app.vasyBus.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService{

    private final ModelMapper modelMapper;
    private final RouteRepository routeRepository;

    @Override
    public RouteResponseDTO addRoute(RouteRequestDTO routeRequestDTO) {
        Route route = Route.builder()
                .sourceCity(routeRequestDTO.getSourceCity())
                .destinationCity(routeRequestDTO.getDestinationCity())
                .distanceKm(routeRequestDTO.getDistanceKm())
                .estimatedTime(routeRequestDTO.getEstimatedTime())
                .build();

         routeRepository.save(route);
        return routeRepository.findRouteById(route.getRouteId());
    }

    @Override
    public List<RouteResponseDTO> getAllRoutes() {
        return routeRepository.findAllRoutes();
    }

    @Override
    public RouteResponseDTO getRouteById(Long id) {
        RouteResponseDTO savedRoute = routeRepository.findRouteById(id);

        if(savedRoute == null){
            throw new ResourceNotFoundException("Route not found with id: " + id);
        }

        return savedRoute;
    }

    @Override
    public RouteResponseDTO updateRoute(Long id, RouteRequestDTO routeRequestDTO) {
       getRouteById(id);
        routeRepository.updateRoute(
                id,
                routeRequestDTO.getSourceCity(),
                routeRequestDTO.getDestinationCity(),
                routeRequestDTO.getDistanceKm(),
                routeRequestDTO.getEstimatedTime()
        );

        return routeRepository.findRouteById(id);
    }

    @Override
    public String deleteRoute(Long id) {
       getRouteById(id);
       routeRepository.softDeleteRoute(id);
        return "Route deleted successfully";

    }


}
