package com.app.vasyBus.model;

import com.app.vasyBus.enums.BusType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "buses", indexes = {
        @Index(name = "idx_bus_number", columnList = "bus_number")
})
@Builder
@DynamicInsert
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
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "bus",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Schedule> schedules;

    @Column(name = "is_deleted",
            nullable = false,
            columnDefinition = "boolean default false")
    private boolean deleted;
}