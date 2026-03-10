package com.app.vasyBus.model;

import com.app.vasyBus.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "booking_seats", indexes = {
        @Index(name = "idx_booking_seat_booking", columnList = "booking_id"),
        @Index(name = "idx_booking_seat_seat", columnList = "seat_id")
})
@Data
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_seat_id")
    private Long bookingSeatId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(
            name = "passenger_name",
            nullable = false,
            length = 100
    )
    private String passengerName;

    @Column(
            name = "passenger_age",
            nullable = false,
            columnDefinition = "INT"
    )
    private Integer passengerAge;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "passenger_gender",
            nullable = false,
            length = 10
    )
    private Gender passengerGender;

    @Column(name = "is_deleted",
            nullable = false,
            columnDefinition = "boolean default false")
    private boolean deleted;
}