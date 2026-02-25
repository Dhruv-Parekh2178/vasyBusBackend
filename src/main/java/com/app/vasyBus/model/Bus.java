package com.app.vasyBus.model;

import com.app.vasyBus.model.enums.BusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_id")
    private Long busId;

    @Column(name = "bus_name", nullable = false)
    private String busName;

    @Column(name = "bus_number", unique = true, nullable = false)
    private String busNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "bus_type", nullable = false)
    private BusType busType;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "operator_name", nullable = false)
    private String operatorName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "bus",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Schedule> schedules;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;
}