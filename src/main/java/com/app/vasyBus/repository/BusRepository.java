package com.app.vasyBus.repository;

import com.app.vasyBus.dto.bus.BusResponseDTO;
import com.app.vasyBus.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long>{

    boolean existsByBusNumber(String busNumber);

    @Query(value = """
        SELECT
            bus_id AS busId,
            bus_name AS busName,
            bus_number AS busNumber,
            bus_type AS busType,
            total_seats AS totalSeats,
            operator_name AS operatorName,
            created_at AS createdAt
        FROM buses
        WHERE is_deleted = false""", nativeQuery = true)
    List<BusResponseDTO> findAllActiveBuses();

    @Query(value = """
            SELECT
                bus_id AS busId,
                bus_name AS busName,
                bus_number AS busNumber,
                bus_type AS busType,
                total_seats AS totalSeats,
                operator_name AS operatorName,
                created_at AS createdAt
            FROM buses
            WHERE bus_id =:id
            AND is_deleted = false""", nativeQuery = true)
    BusResponseDTO findBusById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE buses
        SET
            bus_name =:busName,
            bus_number =:busNumber,
            bus_type =:busType,
            total_seats =:totalSeats,
            operator_name =:operatorName
        WHERE bus_id =:id""", nativeQuery = true)
    void updateBus(@Param("id") Long id,@Param("busName") String busName,
                   @Param("busNumber") String busNumber, @Param("busType") String busType,
                   @Param("totalSeats") Integer totalSeats,@Param("operatorName") String operatorName);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE buses
        SET is_deleted = true
        WHERE bus_id=:id
        """, nativeQuery = true)
    void softDeleteBus(@Param("id") Long id);

}
