package com.app.vasyBus.service.seat;

import com.app.vasyBus.dto.seat.SeatLockRequestDTO;
import com.app.vasyBus.dto.seat.SeatResponseDTO;

import java.util.List;

public interface SeatService {
    List<SeatResponseDTO> getSeatsByScheduleId(Long scheduleId);
    String lockSeat(SeatLockRequestDTO seatLockRequestDTO , Long userId);
    String unlockSeat(Long seatId , Long userId);
}
