package com.app.vasyBus.service.bus;

import com.app.vasyBus.dtos.BusRequestDTO;
import com.app.vasyBus.dtos.BusResponseDTO;

import java.util.List;

public interface BusService {
    BusResponseDTO addBus(BusRequestDTO busRequestDTO);

    List<BusResponseDTO> getAllBuses();

    BusResponseDTO getBusById(Long id);

    BusResponseDTO updateBus(Long id,BusRequestDTO busRequestDTO);

    String deleteBus(Long id);
}
