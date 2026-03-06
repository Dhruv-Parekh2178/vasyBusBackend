package com.app.vasyBus.repository;

import com.app.vasyBus.dto.route.RouteResponseDTO;
import com.app.vasyBus.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route , Long> {

    @Query(value = """
        SELECT
            route_id AS routeId,
          source_city AS sourceCity,
          destination_city AS destinationCity,
          distance_km AS distanceKm,
          estimated_time AS estimatedTime
          FROM routes
          WHERE is_deleted = false
""" , nativeQuery = true)
    List<RouteResponseDTO> findAllRoutes();

    @Query(value = """
        SELECT
            route_id AS routeId,
          source_city AS sourceCity,
          destination_city AS destinationCity,
          distance_km AS distanceKm,
          estimated_time AS estimatedTime
          FROM routes
          WHERE is_deleted = false AND route_id =:id
""" , nativeQuery = true)
    RouteResponseDTO findRouteById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE routes
        SET
           source_city =:sourceCity ,
           destination_city =:destinationCity,
           distance_km =:distanceKm,
           estimated_time =:estimatedTime
        WHERE route_id =:id
""" , nativeQuery = true)
    void updateRoute(@Param("id") Long id,@Param("sourceCity") String sourceCity , @Param("destinationCity") String destinationCity,
                                 @Param("distanceKm") Double distanceKm, @Param("estimatedTime") String estimatedTime);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE routes
        SET is_deleted = true
        WHERE route_id =:id
        """, nativeQuery = true)
    void softDeleteRoute(@Param("id") Long id);
}
