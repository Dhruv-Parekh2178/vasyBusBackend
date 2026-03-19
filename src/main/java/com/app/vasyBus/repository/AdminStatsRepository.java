package com.app.vasyBus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class AdminStatsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long countActiveBuses() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM buses WHERE is_deleted = false",
                Long.class);
    }

    public Long countActiveRoutes() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM routes WHERE is_deleted = false",
                Long.class);
    }

    public Long countActiveSchedules() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM schedules WHERE is_deleted = false AND schedule_status = 'ACTIVE'",
                Long.class);
    }

    public Long countTotalBookings() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM bookings WHERE is_deleted = false",
                Long.class);
    }

    public Long countBookingsByStatus(String status) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM bookings WHERE booking_status = ? AND is_deleted = false",
                Long.class, status);
    }

    public Long countTotalUsers() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM app_users",
                Long.class);
    }

    public Long countBookingsToday() {
        return jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*) FROM bookings
                WHERE DATE(created_at AT TIME ZONE 'Asia/Kolkata') = CURRENT_DATE
                AND is_deleted = false
                """,
                Long.class);
    }

    public BigDecimal revenueToday() {
        BigDecimal result = jdbcTemplate.queryForObject(
                """
                SELECT COALESCE(SUM(p.amount), 0)
                FROM payments p
                JOIN bookings b ON b.booking_id = p.booking_id
                WHERE p.payment_status = 'SUCCESS'
                AND DATE(p.created_at AT TIME ZONE 'Asia/Kolkata') = CURRENT_DATE
                AND p.is_deleted = false
                """,
                BigDecimal.class);
        return result != null ? result : BigDecimal.ZERO;
    }

    public BigDecimal revenueTotal() {
        BigDecimal result = jdbcTemplate.queryForObject(
                """
                SELECT COALESCE(SUM(amount), 0)
                FROM payments
                WHERE payment_status = 'SUCCESS'
                AND is_deleted = false
                """,
                BigDecimal.class);
        return result != null ? result : BigDecimal.ZERO;
    }
}