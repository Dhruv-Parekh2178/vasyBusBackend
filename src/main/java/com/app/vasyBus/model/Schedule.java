package com.app.vasyBus.model;

import com.app.vasyBus.model.enums.ScheduleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @OneToMany(mappedBy = "schedule",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Seat> seats;

    @OneToMany(mappedBy = "schedule",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Booking> bookings;

    @Column(name = "departure_time", nullable = false)
    private Instant departureTime;

    @Column(name = "arrival_time", nullable = false)
    private Instant arrivalTime;

    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;

    @Column(name = "price_per_seat", nullable = false)
    private BigDecimal pricePerSeat;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_status", nullable = false)
    private ScheduleStatus scheduleStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;
}