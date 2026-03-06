package com.app.vasyBus.model;

import com.app.vasyBus.enums.BookingStatus;
import com.app.vasyBus.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_booking_user", columnList = "user_id"),
        @Index(name = "idx_booking_schedule", columnList = "schedule_id"),
        @Index(name = "idx_booking_status", columnList = "booking_status")
})
@DynamicInsert
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @OneToMany(mappedBy = "booking",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<BookingSeat> bookingSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @OneToOne(mappedBy = "booking",
            cascade = CascadeType.ALL)
    private Payment payment;

    @Column(
            name = "total_amount",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false, length = 30,columnDefinition = "VARCHAR(30) DEFAULT 'PENDING'")
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    private PaymentStatus paymentStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}