package com.app.vasyBus.service.bus;

import com.app.vasyBus.dto.bus.BusRequestDTO;
import com.app.vasyBus.dto.bus.BusResponseDTO;

import java.util.List;

public interface BusService {
    BusResponseDTO addBus(BusRequestDTO busRequestDTO);

    List<BusResponseDTO> getAllBuses();

    BusResponseDTO getBusById(Long id);

    BusResponseDTO updateBus(Long id,BusRequestDTO busRequestDTO);

    String deleteBus(Long id);
}
