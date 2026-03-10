package com.app.vasyBus.repository;

import com.app.vasyBus.dto.booking.BookingResponseDTO;
import com.app.vasyBus.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking , Long> {

    @Query(value = """
        SELECT
            bk.booking_id AS bookingId,
            u.user_id AS userId,
            u.name AS userName,
            s.schedule_id AS scheduleId,
            b.bus_name AS busName,
            b.bus_type AS busType,
            r.source_city AS sourceCity,
            r.destination_city AS destinationCity,
            s.departure_time AS departureTime,
            s.arrival_time AS arrivalTime,
            s.travel_date AS travelDate,
            bk.total_amount AS totalAmount,
            bk.booking_status AS bookingStatus,
            bk.payment_status AS paymentStatus,
            bk.cancelled_by AS cancelledBy,
            bk.cancellation_reason AS cancellationReason,
            bk.created_at AS createdAt
        FROM bookings bk
        JOIN app_users u ON bk.user_id = u.user_id
        JOIN schedules s ON bk.schedule_id = s.schedule_id
        JOIN buses b ON s.bus_id = b.bus_id
        JOIN routes r ON s.route_id = r.route_id
        WHERE bk.booking_id =:bookingId
        AND bk.is_deleted=false
""",nativeQuery = true)
    BookingResponseDTO findBookingsById(@Param("bookingId")Long bookingId);


@Query(value = """
        SELECT
            bk.booking_id AS bookingId,
            u.user_id AS userId,
            u.name AS userName,
            s.schedule_id AS scheduleId,
            b.bus_name AS busName,
            b.bus_type AS busType,
            r.source_city AS sourceCity,
            r.destination_city AS destinationCity,
            s.departure_time AS departureTime,
            s.arrival_time AS arrivalTime,
            s.travel_date AS travelDate,
            bk.total_amount AS totalAmount,
            bk.booking_status AS bookingStatus,
            bk.payment_status AS paymentStatus,
            bk.cancelled_by AS cancelledBy,
            bk.cancellation_reason AS cancellationReason,
            bk.created_at AS createdAt
        FROM bookings bk
        JOIN app_users u ON bk.user_id = u.user_id
        JOIN schedules s ON bk.schedule_id = s.schedule_id
        JOIN buses b ON s.bus_id = b.bus_id
        JOIN routes r ON s.route_id = r.route_id
        WHERE bk.user_id =:userId
        AND bk.is_deleted=false
        ORDER BY bk.created_at DESC
""",nativeQuery = true)
List<BookingResponseDTO> findBookingsByUserId(@Param("userId") Long userId);


@Query(value = """
        SELECT
            bk.booking_id AS bookingId,
            u.user_id AS userId,
            u.name AS userName,
            s.schedule_id AS scheduleId,
            b.bus_name AS busName,
            b.bus_type AS busType,
            r.source_city AS sourceCity,
            r.destination_city AS destinationCity,
            s.departure_time AS departureTime,
            s.arrival_time AS arrivalTime,
            s.travel_date AS travelDate,
            bk.total_amount AS totalAmount,
            bk.booking_status AS bookingStatus,
            bk.payment_status AS paymentStatus,
            bk.cancelled_by AS cancelledBy,
            bk.cancellation_reason AS cancellationReason,
            bk.created_at AS createdAt
        FROM bookings bk
        JOIN app_users u ON bk.user_id = u.user_id
        JOIN schedules s ON bk.schedule_id = s.schedule_id
        JOIN buses b ON s.bus_id = b.bus_id
        JOIN routes r ON s.route_id = r.route_id
        WHERE bk.is_deleted=false
        ORDER BY bk.created_at DESC
""",nativeQuery = true)
List<BookingResponseDTO> findAllBookings();

    @Query(value = """
        SELECT
            bk.booking_id AS bookingId,
            u.user_id AS userId,
            u.name AS userName,
            s.schedule_id AS scheduleId,
            b.bus_name AS busName,
            b.bus_type AS busType,
            r.source_city AS sourceCity,
            r.destination_city AS destinationCity,
            s.departure_time AS departureTime,
            s.arrival_time AS arrivalTime,
            s.travel_date AS travelDate,
            bk.total_amount AS totalAmount,
            bk.booking_status AS bookingStatus,
            bk.payment_status AS paymentStatus,
            bk.cancelled_by AS cancelledBy,
            bk.cancellation_reason AS cancellationReason,
            bk.created_at AS createdAt
        FROM bookings bk
        JOIN app_users u ON bk.user_id = u.user_id
        JOIN schedules s ON bk.schedule_id = s.schedule_id
        JOIN buses b ON s.bus_id = b.bus_id
        JOIN routes r ON s.route_id = r.route_id
        WHERE bk.user_id =:userId
        AND bk.is_deleted=false
        ORDER BY bk.created_at DESC
""",nativeQuery = true)
    List<BookingResponseDTO> findBookingsByScheduleId(@Param("scheduleId") Long scheduleId);

    @Query(value = """
             SELECT
            bk.booking_id AS bookingId,
            u.user_id AS userId,
            u.name AS userName,
            s.schedule_id AS scheduleId,
            b.bus_name AS busName,
            b.bus_type AS busType,
            r.source_city AS sourceCity,
            r.destination_city AS destinationCity,
            s.departure_time AS departureTime,
            s.arrival_time AS arrivalTime,
            s.travel_date AS travelDate,
            bk.total_amount AS totalAmount,
            bk.booking_status AS bookingStatus,
            bk.payment_status AS paymentStatus,
            bk.cancelled_by AS cancelledBy,
            bk.cancellation_reason AS cancellationReason,
            bk.created_at AS createdAt
        FROM bookings bk
        JOIN app_users u ON bk.user_id = u.user_id
        JOIN schedules s ON bk.schedule_id = s.schedule_id
        JOIN buses b ON s.bus_id = b.bus_id
        JOIN routes r ON s.route_id = r.route_id
        WHERE bk.booking_status =:status
        AND bk.is_deleted=false
        ORDER BY bk.created_at DESC
        """, nativeQuery = true)
    List<BookingResponseDTO> findBookingsByStatus(@Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE bookings
          SET booking_status = 'CANCELLED',
              cancelled_by =:cancelledBy,
              cancellation_reason =:reason,
              updated_at = now()
        WHERE booking_id =:bookingId
        AND is_deleted = false
""",nativeQuery = true)
    void cancelBooking(@Param("bookingId")Long bookingId,
                       @Param("cancelledBy")String cancelledBy,
                       @Param("reason") String reason
                       );

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE bookings
            SET payment_status  = :paymentStatus,
                booking_status  = :bookingStatus,
                updated_at      = NOW()
            WHERE booking_id = :bookingId
            AND is_deleted = false
            """, nativeQuery = true)
    void updateBookingStatuses(
            @Param("bookingId") Long bookingId,
            @Param("paymentStatus") String paymentStatus,
            @Param("bookingStatus") String bookingStatus);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE bookings
            SET is_deleted = true,
                updated_at = NOW()
            WHERE booking_id = :bookingId
            """, nativeQuery = true)
    void softDeleteBooking(@Param("bookingId") Long bookingId);

    @Query(value = """
            SELECT COUNT(*) > 0
            FROM bookings
            WHERE booking_id = :bookingId
            AND is_deleted = false
            """, nativeQuery = true)
    boolean existsActiveBookingById(@Param("bookingId") Long bookingId);

}