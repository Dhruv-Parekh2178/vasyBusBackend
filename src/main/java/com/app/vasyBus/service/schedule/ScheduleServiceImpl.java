package com.app.vasyBus.service.schedule;

import com.app.vasyBus.dto.schedule.ScheduleRequestDTO;
import com.app.vasyBus.dto.schedule.ScheduleResponseDTO;
import com.app.vasyBus.dto.schedule.ScheduleSearchDTO;
import com.app.vasyBus.enums.BusType;
import com.app.vasyBus.enums.ScheduleStatus;
import com.app.vasyBus.enums.SeatType;
import com.app.vasyBus.exception.ResourceNotFoundException;
import com.app.vasyBus.model.Bus;
import com.app.vasyBus.model.Route;
import com.app.vasyBus.model.Schedule;
import com.app.vasyBus.model.Seat;
import com.app.vasyBus.repository.BusRepository;
import com.app.vasyBus.repository.RouteRepository;
import com.app.vasyBus.repository.ScheduleRepository;
import com.app.vasyBus.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;    private final SeatRepository seatRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;

    @Override
    @Transactional
    public ScheduleResponseDTO createSchedule(ScheduleRequestDTO scheduleRequestDTO) {

        Bus bus = busRepository.findById(scheduleRequestDTO.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bus not found with id: " + scheduleRequestDTO.getBusId()));

        if (bus.isDeleted()) {
            throw new ResourceNotFoundException(
                    "Bus not found with id: " + scheduleRequestDTO.getBusId());
        }

        Route route = routeRepository.findById(scheduleRequestDTO.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Route not found with id: " + scheduleRequestDTO.getRouteId()));

        Schedule schedule = Schedule.builder()
                .bus(bus)
                .route(route)
                .departureTime(scheduleRequestDTO.getDepartureTime())
                .arrivalTime(scheduleRequestDTO.getArrivalTime())
                .travelDate(scheduleRequestDTO.getTravelDate())
                .pricePerSeat(scheduleRequestDTO.getPricePerSeat())
                .scheduleStatus(ScheduleStatus.ACTIVE)
                .build();

        Schedule savedSchedule = scheduleRepository.save(schedule);

        generateSeats(savedSchedule, bus);

        return scheduleRepository.findScheduleById(savedSchedule.getScheduleId());
    }

    @Override
    public List<ScheduleResponseDTO> getAllSchedules() {
        return scheduleRepository.findAllActiveSchedules();
    }

    @Override
    public ScheduleResponseDTO getScheduleById(Long id) {
        ScheduleResponseDTO schedule = scheduleRepository.findScheduleById(id);
        if (schedule == null) {
            throw new ResourceNotFoundException(
                    "Schedule not found with id: " + id);
        }
        return schedule;
    }

    @Override
    public List<ScheduleSearchDTO> searchSchedules(
            String source, String destination, LocalDate travelDate) {
        return scheduleRepository.searchSchedules(source, destination, travelDate);
    }

    @Override
    public ScheduleResponseDTO updateSchedule(Long id, ScheduleRequestDTO scheduleRequestDTO) {
        if (!scheduleRepository.existsActiveScheduleById(id)) {
            throw new ResourceNotFoundException(
                    "Schedule not found with id: " + id);
        }
        scheduleRepository.updateSchedule(
                id,
                scheduleRequestDTO.getDepartureTime(),
                scheduleRequestDTO.getArrivalTime(),
                scheduleRequestDTO.getTravelDate(),
                scheduleRequestDTO.getPricePerSeat()
        );
        return scheduleRepository.findScheduleById(id);
    }

    @Override
    public String deleteSchedule(Long id) {
        if (!scheduleRepository.existsActiveScheduleById(id)) {
            throw new ResourceNotFoundException(
                    "Schedule not found with id: " + id);
        }
        seatRepository.softDeleteSeatsByScheduleId(id);
        scheduleRepository.softDeleteSchedule(id);
        return "Schedule deleted successfully";
    }

    private void generateSeats(Schedule schedule, Bus bus) {
        List<Seat> seats = new ArrayList<>();
        BusType busType = bus.getBusType();
        int totalSeats = bus.getTotalSeats();

        if (busType == BusType.AC_SLEEPER || busType == BusType.NON_AC_SLEEPER) {
            seats = generateSleeperSeats(schedule, totalSeats);
        } else {
            seats = generateSeaterSeats(schedule, busType, totalSeats);
        }

        seatRepository.saveAll(seats);
    }

    private List<Seat> generateSeaterSeats(Schedule schedule,
                                           BusType busType,
                                           int totalSeats) {
        List<Seat> seats = new ArrayList<>();
        int seatsPerRow = (busType == BusType.AC_SEATER_2X2
                || busType == BusType.NON_AC_SEATER_2X2) ? 4 : 5;

        for (int i = 1; i <= totalSeats; i++) {
            int rowIndex    = (i - 1) / seatsPerRow;
            int colPosition = (i - 1) % seatsPerRow + 1;
            char rowLetter  = (char) ('A' + rowIndex);
            String seatNumber = "" + rowLetter + colPosition;

            seats.add(Seat.builder()
                    .schedule(schedule)
                    .seatNumber(seatNumber)
                    .seatType(resolveSeaterSeatType(seatsPerRow, colPosition))
                    .booked(false)
                    .build());
        }
        return seats;
    }

    private SeatType resolveSeaterSeatType(int seatsPerRow, int col) {
        if (seatsPerRow == 4) {
            return (col == 1 || col == 4) ? SeatType.WINDOW : SeatType.AISLE;
        } else {
            return switch (col) {
                case 1, 5 -> SeatType.WINDOW;
                case 4    -> SeatType.MIDDLE;
                default   -> SeatType.AISLE;
            };
        }
    }

    private List<Seat> generateSleeperSeats(Schedule schedule, int totalSeats) {
        List<Seat> seats = new ArrayList<>();

        int halfCount = totalSeats / 2;

        for (int i = 1; i <= halfCount; i++) {
            seats.add(Seat.builder()
                    .schedule(schedule)
                    .seatNumber("L" + i)
                    .seatType(SeatType.LOWER)
                    .booked(false)
                    .build());
        }

        for (int i = 1; i <= halfCount; i++) {
            seats.add(Seat.builder()
                    .schedule(schedule)
                    .seatNumber("U" + i)
                    .seatType(SeatType.UPPER)
                    .booked(false)
                    .build());
        }
        return seats;
    }
}