package com.app.vasyBus.service.route;

import com.app.vasyBus.dto.route.RouteRequestDTO;
import com.app.vasyBus.dto.route.RouteResponseDTO;
import com.app.vasyBus.exception.ResourceNotFoundException;
import com.app.vasyBus.model.Route;
import com.app.vasyBus.repository.RouteRepository;
import com.app.vasyBus.repository.ScheduleRepository;
import com.app.vasyBus.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService{

    private final RouteRepository routeRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CITY_CACHE_PREFIX = "city:suggest:";

    @Override
    public RouteResponseDTO addRoute(RouteRequestDTO routeRequestDTO) {
        Route route = Route.builder()
                .sourceCity(routeRequestDTO.getSourceCity())
                .destinationCity(routeRequestDTO.getDestinationCity())
                .distanceKm(routeRequestDTO.getDistanceKm())
                .estimatedTime(routeRequestDTO.getEstimatedTime())
                .build();

         routeRepository.save(route);
        Set<String> keys = redisTemplate.keys(CITY_CACHE_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
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
        Set<String> keys = redisTemplate.keys(CITY_CACHE_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        return routeRepository.findRouteById(id);
    }

    @Override
    @Transactional
    public String deleteRoute(Long id) {

        getRouteById(id);
        List<Long> scheduleIds =
                scheduleRepository.findActiveScheduleIdsByRouteId(id);
        if (!scheduleIds.isEmpty()) {
            scheduleIds.forEach(
                    scheduleId -> seatRepository
                            .softDeleteSeatsByScheduleId(scheduleId));
        }
        scheduleRepository.softDeleteSchedulesByRouteId(id);
        routeRepository.softDeleteRoute(id);

        return "Route deleted successfully";
    }


}
