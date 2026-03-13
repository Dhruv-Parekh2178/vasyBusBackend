package com.app.vasyBus.repository;

import com.app.vasyBus.dto.payment.PaymentResponseDTO;
import com.app.vasyBus.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = """
            SELECT * FROM payments
            WHERE stripe_payment_id = :stripePaymentId
            AND is_deleted = false
            """, nativeQuery = true)
    Optional<Payment> findByStripePaymentId(
            @Param("stripePaymentId") String stripePaymentId);

    @Query(value = """
            SELECT
                p.payment_id AS paymentId,
                p.booking_id AS bookingId,
                p.stripe_payment_id AS stripePaymentId,
                p.amount AS amount,
                p.currency AS currency,
                p.payment_status AS paymentStatus,
                p.created_at AS createdAt
            FROM payments p
            WHERE p.booking_id = :bookingId
            AND p.is_deleted = false
            """, nativeQuery = true)
    PaymentResponseDTO findPaymentByBookingId(
            @Param("bookingId") Long bookingId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE payments
            SET payment_status = :status
            WHERE stripe_payment_id = :stripePaymentId
            AND is_deleted = false
            """, nativeQuery = true)
    void updatePaymentStatus(
            @Param("stripePaymentId") String stripePaymentId,
            @Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE payments
            SET is_deleted = true
            WHERE payment_id = :paymentId
            """, nativeQuery = true)
    void softDeletePayment(@Param("paymentId") Long paymentId);
}