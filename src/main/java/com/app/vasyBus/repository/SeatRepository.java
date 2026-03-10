package com.app.vasyBus.repository;

import com.app.vasyBus.dto.seat.SeatResponseDTO;
import com.app.vasyBus.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat , Long> {

    @Query(value = """
        SELECT
        s.seat_id AS seatId,
        s.schedule_id AS scheduleId,
        s.seat_number AS seatNumber,
        s.seat_type AS seatType,
        s.booked AS booked,
          CASE 
             WHEN s.booked = true THEN 'BOOKED'
             ELSE 'AVAILABLE'
        END AS seatStatus,
            s.created_at AS createdAt
        FROM seats s 
        WHERE s.schedule_id =:scheduleId
         AND s.is_deleted = false
        ORDER BY s.seat_number ASC
""",nativeQuery = true)
    List<SeatResponseDTO> findSeatByScheduleId(@Param("scheduleId") Long scheduleId);

    @Query(value = """
        SELECT
        s.seat_id AS seatId,
        s.schedule_id AS scheduleId,
        s.seat_number AS seatNumber,
        s.seat_type AS seatType,
        s.booked AS booked,
          CASE
             WHEN s.booked = true THEN 'BOOKED'
             ELSE 'AVAILABLE'
        END AS seatStatus,
            s.created_at AS createdAt
        FROM seats s
        WHERE s.seat_id =:seatId
""",nativeQuery = true)
    SeatResponseDTO findSeatById(@Param("seatId") Long seatId);

    @Query(value = """
            SELECT COUNT(*) FROM seats WHERE schedule_id =:scheduleId AND booked = false  AND is_deleted = false
            """, nativeQuery = true)
    Integer countAvailableSeats(@Param("scheduleId") Long scheduleId);


    @Modifying
    @Transactional
    @Query(value = """
            UPDATE seats SET booked = true WHERE seat_id =:seatId
            """, nativeQuery = true)
    void markSeatAsBooked(@Param("seatId") Long seatId);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE seats SET booked = false WHERE seat_id=:seatId
""",nativeQuery = true)
   void markSeatAsAvailable(@Param("seatId")Long seatId);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE seats
        SET is_deleted = true
        WHERE schedule_id = :scheduleId
        AND is_deleted = false
        """, nativeQuery = true)
    void softDeleteSeatsByScheduleId(@Param("scheduleId") Long scheduleId);

    @Query(value = """
        SELECT
            s.seat_id       AS seatId,
            s.schedule_id   AS scheduleId,
            s.seat_number   AS seatNumber,
            s.seat_type     AS seatType,
            s.booked        AS booked,
            CASE
                WHEN s.booked = true THEN 'BOOKED'
                ELSE 'AVAILABLE'
            END             AS seatStatus,
            s.created_at    AS createdAt
        FROM seats s
        WHERE s.seat_id = :seatId
        AND s.is_deleted = false
        """, nativeQuery = true)
    SeatResponseDTO findSeatBySeatId(@Param("seatId") Long seatId);
}
