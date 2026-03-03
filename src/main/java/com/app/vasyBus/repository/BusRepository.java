package com.app.vasyBus.repository;

import com.app.vasyBus.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus, Long>{

    boolean existsByBusNumber(String busNumber);
}
