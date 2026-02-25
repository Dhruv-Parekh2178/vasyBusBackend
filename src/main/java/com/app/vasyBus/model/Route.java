package com.app.vasyBus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routes")
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
    private String estimatedTime;   // keep as String if using HH:mm

    @OneToMany(mappedBy = "route",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Schedule> schedules;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;
}