package com.app.vasyBus.model;

import com.app.vasyBus.enums.CurrencyType;
import com.app.vasyBus.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments", indexes = {
        @Index(name = "idx_payment_booking", columnList = "booking_id"),
        @Index(name = "idx_payment_stripe", columnList = "stripe_payment_id"),
        @Index(name = "idx_payment_status", columnList = "payment_status")
})
@Builder
@DynamicInsert
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "stripe_payment_id", unique = true)
    private String stripePaymentId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency",
            nullable = false,
            columnDefinition = "varchar(10) default 'INR'")
    private CurrencyType currency =CurrencyType.INR;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status",
            nullable = false,
            columnDefinition = "varchar(20) default 'PENDING'")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "is_deleted",
            nullable = false,
            columnDefinition = "boolean default false")
    private boolean deleted;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}