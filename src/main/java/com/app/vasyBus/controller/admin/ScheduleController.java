package com.app.vasyBus.controller.admin;

import com.app.vasyBus.dto.schedule.ScheduleRequestDTO;
import com.app.vasyBus.dto.schedule.ScheduleResponseDTO;
import com.app.vasyBus.service.schedule.ScheduleService;
import com.app.vasyBus.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/add/schedule")
    public ResponseEntity<ApiResponse<ScheduleResponseDTO>> addSchedule(@Valid @RequestBody ScheduleRequestDTO scheduleRequestDTO){
        ScheduleResponseDTO responseDTO = scheduleService.createSchedule(scheduleRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDTO));
    }

    @GetMapping("/schedules")
    public ResponseEntity<ApiResponse<List<ScheduleResponseDTO>>> getAllSchedules(){
        List<ScheduleResponseDTO> responseDTOS = scheduleService.getAllSchedules();
        return ResponseEntity.ok(ApiResponse.success(responseDTOS));
    }

    @GetMapping("/admin/schedule/{id}")
    public ResponseEntity<ApiResponse<ScheduleResponseDTO>> getScheduleById(@PathVariable Long id){
        ScheduleResponseDTO responseDTO = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }


    @PutMapping("/update/schedule/{id}")
    public ResponseEntity<ApiResponse<ScheduleResponseDTO>> updateSchedule(@PathVariable Long id,@Valid @RequestBody ScheduleRequestDTO scheduleRequestDTO){
        ScheduleResponseDTO responseDTO = scheduleService.updateSchedule(id ,scheduleRequestDTO);
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

    @DeleteMapping("/delete/schedule/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSchedule(@PathVariable Long id){
        String response = scheduleService.deleteSchedule(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
