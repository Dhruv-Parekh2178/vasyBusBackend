package com.app.vasyBus.service.bus;

import com.app.vasyBus.dtos.BusRequestDTO;
import com.app.vasyBus.dtos.BusResponseDTO;
import com.app.vasyBus.exception.BusAlreadyExistsException;
import com.app.vasyBus.exception.ResourceNotFoundException;
import com.app.vasyBus.model.Bus;
import com.app.vasyBus.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        Bus bus = modelMapper.map(busRequestDTO , Bus.class);
        bus = busRepository.save(bus);
        return modelMapper.map(bus , BusResponseDTO.class);
    }

    @Override
        public List<BusResponseDTO> getAllBuses() {
            List<Bus> buses = busRepository.findAll().stream()
                    .filter(bus -> !bus.isDeleted())
                    .toList();

        if (buses.isEmpty()) {
            return Collections.emptyList();
        }


        return buses.stream().map(bus -> modelMapper.map(bus , BusResponseDTO.class)).toList();
        }

    @Override
    public BusResponseDTO getBusById(Long id) {
      Bus savedBus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));

      return modelMapper.map(savedBus , BusResponseDTO.class);
    }

    @Override
    public BusResponseDTO updateBus(Long id, BusRequestDTO busRequestDTO) {
        Bus savedBus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));

        savedBus.setBusName(busRequestDTO.getBusName());
        savedBus.setBusNumber(busRequestDTO.getBusNumber());
        savedBus.setBusType(busRequestDTO.getBusType());
        savedBus.setTotalSeats(busRequestDTO.getTotalSeats());
        savedBus.setOperatorName(busRequestDTO.getOperatorName());

        Bus updatedBus = busRepository.save(savedBus);

        return modelMapper.map(updatedBus , BusResponseDTO.class);
    }

    @Override
    public String deleteBus(Long id) {

        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));

        busRepository.delete(bus);

        return "Bus deleted successfully";
    }


}
