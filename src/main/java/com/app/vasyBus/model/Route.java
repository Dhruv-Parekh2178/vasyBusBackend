package com.app.vasyBus.model;

import com.app.vasyBus.model.Schedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routes", indexes = {
        @Index(name = "idx_route_cities", columnList = "source_city, destination_city")
})
@Builder
@DynamicInsert
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "source_city", nullable = false)
    private String sourceCity;

    @Column(name = "destination_city", nullable = false)
    private String destinationCity;

    @Column(name = "distance_km", nullable = false)
    private Double distanceKm;

    @Column(name = "estimated_time", nullable = false)
    private String estimatedTime;

    @OneToMany(mappedBy = "route",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Schedule> schedules;

    @Column(name = "is_deleted",
            nullable = false,
            columnDefinition = "boolean default false")
    private boolean deleted;
}