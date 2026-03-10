package com.app.vasyBus.controller.publicUser;

import com.app.vasyBus.dto.schedule.ScheduleResponseDTO;
import com.app.vasyBus.dto.schedule.ScheduleSearchDTO;
import com.app.vasyBus.service.schedule.ScheduleService;
import com.app.vasyBus.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicScheduleController {
    private final ScheduleService scheduleService;
    @GetMapping("/schedule/{id}")
    public ResponseEntity<ApiResponse<ScheduleResponseDTO>> getScheduleById(@PathVariable Long id){
        ScheduleResponseDTO responseDTO = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

    @GetMapping("/schedules/search")
    public ResponseEntity<ApiResponse<List<ScheduleSearchDTO>>> searchSchedule(@RequestParam String source, @RequestParam String destination,
                                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate travelDate){
        List<ScheduleSearchDTO> searchDTOS = scheduleService.searchSchedules(source, destination, travelDate);
        return ResponseEntity.ok(ApiResponse.success(searchDTOS));
    }
}
