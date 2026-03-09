package com.app.vasyBus.service.schedule;

import com.app.vasyBus.dto.schedule.ScheduleRequestDTO;
import com.app.vasyBus.dto.schedule.ScheduleResponseDTO;
import com.app.vasyBus.dto.schedule.ScheduleSearchDTO;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    ScheduleResponseDTO createSchedule(ScheduleRequestDTO dto);
    List<ScheduleResponseDTO> getAllSchedules();
    ScheduleResponseDTO getScheduleById(Long id);
    List<ScheduleSearchDTO> searchSchedules(String source, String destination, LocalDate travelDate);
    ScheduleResponseDTO updateSchedule(Long id, ScheduleRequestDTO dto);
    String deleteSchedule(Long id);
}