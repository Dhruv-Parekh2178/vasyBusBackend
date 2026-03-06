package com.app.vasyBus.repository;

import com.app.vasyBus.dto.schedule.ScheduleResponseDTO;
import com.app.vasyBus.dto.schedule.ScheduleSearchDTO;
import com.app.vasyBus.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule , Long> {

    @Query(value = """
        SELECT
            s.schedule_id AS scheduleId,
            b.bus_id AS busId,
            b.bus_name AS busName,
            b.bus_type AS busType,
            r.route_id AS routeId,
            r.source_city AS sourceCity,
            r.destination_city AS destinationCity,
            s.departure_time AS departureTime,
            s.arrival_time AS arrivalTime,
            s.travel_date AS travelDate,
            s.price_per_seat AS pricePerSeat,
            s.schedule_status AS scheduleStatus,
            s.created_at AS createdAt
            FROM schedules s
        JOIN buses b ON s.bus_id = b.bus_id
        JOIN routes ON s.route_id = r.route_id
        WHERE s.is_deleted = false
        ORDER BY s.travel_date ASC, s.departure_time ASC
""",nativeQuery = true)
    List<ScheduleResponseDTO> findAllActiveSchedules();

    @Query(value = """
        SELECT
            s.schedule_id AS scheduleId,
            b.bus_id AS busId,
            b.bus_name AS busName,
            b.bus_type AS busType,
            r.route_id AS routeId,
            r.source_city AS sourceCity,
            r.destination_city AS destinationCity,
            s.departure_time AS departureTime,
            s.arrival_time AS arrivalTime,
            s.travel_date AS travelDate,
            s.price_per_seat AS pricePerSeat,
            s.schedule_status AS scheduleStatus,
            s.created_at AS createdAt
            FROM schedules s
        JOIN buses b ON s.bus_id = b.bus_id
        JOIN routes ON s.route_id = r.route_id
        WHERE s.is_deleted = false AND s.schedule_id =:id
""",nativeQuery = true)
    ScheduleResponseDTO findScheduleById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE Schedules 
          SET 
              departure_time =:departureTime,
              arrival_time =:arrivalTime,
              travel_date =:travelDate,
              price_per_seat=:pricePerSeat
           WHERE s.is_deleted = false AND s.schedule_id =:id
""",nativeQuery = true)
    void updateSchedule(@Param("id") Long id, @Param("departureTime")Instant departureTime ,
                        @Param("arrivalTime") Instant arrivalTime, @Param("travelDate")LocalDate travelDate,
                        @Param("pricePerSeat")BigDecimal pricePerSeat);

    @Modifying
    @Transactional
    @Query(value = """
       UPDATE schedules
          SET 
              is_deleted = true
          WHERE s.schedule_id =:id
""",nativeQuery = true)
    void softDeleteSchedule(@Param("id") Long id);

    @Query(value = """
            SELECT COUNT(*) > 0
            FROM schedules
            WHERE schedule_id = :id
            AND is_deleted = false
            """, nativeQuery = true)
    boolean existsActiveScheduleById(@Param("id") Long id);

    @Query(value = """
            SELECT
                s.schedule_id AS scheduleId,
                b.bus_name AS busName,
                b.bus_type  AS busType,
                b.operator_name AS operatorName,
                r.source_city AS sourceCity,
                r.destination_city AS destinationCity,
                s.departure_time AS departureTime,
                s.arrival_time AS arrivalTime,
                s.travel_date AS travelDate,
                s.price_per_seat AS pricePerSeat,
                COUNT(se.seat_id) FILTER (WHERE se.booked = false) AS availableSeats,
                s.schedule_status AS status
            FROM schedules s
            JOIN buses b ON s.bus_id = b.bus_id
            JOIN routes r ON s.route_id = r.route_id
            JOIN seats se ON se.schedule_id = s.schedule_id
            WHERE LOWER(r.source_city) = LOWER(:source)
            AND LOWER(r.destination_city) = LOWER(:destination)
            AND s.travel_date = :travelDate
            AND s.schedule_status = 'ACTIVE'
            AND s.is_deleted = false
            GROUP BY s.schedule_id, b.bus_name, b.bus_type,
                     b.operator_name, r.source_city, r.destination_city,
                     s.departure_time, s.arrival_time,
                     s.travel_date, s.price_per_seat, s.schedule_status
            """, nativeQuery = true)
    List<ScheduleSearchDTO> searchSchedules(
            @Param("source") String source,
            @Param("destination") String destination,
            @Param("travelDate") LocalDate travelDate
    );

}
