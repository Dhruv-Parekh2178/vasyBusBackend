package com.app.vasyBus.repository;

import com.app.vasyBus.dto.bookingSeat.BookingSeatResponseDTO;
import com.app.vasyBus.model.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat , Long> {

    @Query(value = """
        SELECT
            bs.booking_seat_id AS bookingSeatId,
            bs.booking_id AS bookingId,
            s.seat_id AS seatId,
            s.seat_number AS seatNumber,
            s.seat_type AS seatType,
            bs.passenger_name AS passengerName,
            bs.passenger_age AS passengerAge,
            bs.passenger_gender AS passengerGender
        FROM booking_seats bs
        JOIN seats s ON bs.seat_id = s.seat_id
        WHERE bs.booking_id =:bookingId
        AND bs.is_deleted = false
""" ,nativeQuery = true)
    List<BookingSeatResponseDTO> findAllPassengersByBookingId(@Param("bookingId")Long bookingId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE booking_seats SET is_deleted = true WHERE booking_id = :bookingId AND is_deleted = false
""", nativeQuery = true)
    void softDeleteByBookingId(@Param("bookingId") Long bookingId);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM booking_seats
        WHERE booking_id = :bookingId
    """, nativeQuery = true)
    void hardDeleteByBookingId(@Param("bookingId") Long bookingId);
}
