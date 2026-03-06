package com.app.vasyBus.service.bus;

import com.app.vasyBus.dto.bus.BusRequestDTO;
import com.app.vasyBus.dto.bus.BusResponseDTO;
import com.app.vasyBus.exception.BusAlreadyExistsException;
import com.app.vasyBus.exception.ResourceNotFoundException;
import com.app.vasyBus.model.Bus;
import com.app.vasyBus.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusServiceImpl implements BusService{

    private final BusRepository busRepository;
    private final ModelMapper modelMapper;

    @Override
    public BusResponseDTO addBus(BusRequestDTO busRequestDTO) {
        if (busRepository.existsByBusNumber((busRequestDTO.getBusNumber()))){
            throw new BusAlreadyExistsException("Bus number already exists");
        }

        Bus bus = Bus.builder()
                .busName(busRequestDTO.getBusName())
                .busNumber(busRequestDTO.getBusNumber())
                .busType(busRequestDTO.getBusType())
                .totalSeats(busRequestDTO.getTotalSeats())
                .operatorName(busRequestDTO.getOperatorName())
                .build();

         busRepository.save(bus);

         return busRepository.findBusById(bus.getBusId());
    }

    @Override
        public List<BusResponseDTO> getAllBuses() {
        return busRepository.findAllActiveBuses();
        }

    @Override
    public BusResponseDTO getBusById(Long id) {
        BusResponseDTO savedBus = busRepository.findBusById(id);

        if(savedBus == null){
            throw new ResourceNotFoundException("Bus not found with id: " + id);
        }

        return savedBus;
    }

    @Override
    public BusResponseDTO updateBus(Long id, BusRequestDTO busRequestDTO) {
    getBusById(id);
        busRepository.updateBus(
                id,
                busRequestDTO.getBusName(),
                busRequestDTO.getBusNumber(),
                busRequestDTO.getBusType().name(),
                busRequestDTO.getTotalSeats(),
                busRequestDTO.getOperatorName()
        );

        return busRepository.findBusById(id);
    }

    @Override
    public String deleteBus(Long id) {

        getBusById(id);

        busRepository.softDeleteBus(id);

        return "Bus deleted successfully";
    }


}
