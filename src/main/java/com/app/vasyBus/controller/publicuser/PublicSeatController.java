package com.app.vasyBus.controller.publicuser;

import com.app.vasyBus.dto.seat.SeatResponseDTO;
import com.app.vasyBus.service.seat.SeatService;
import com.app.vasyBus.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicSeatController {

    private final SeatService seatService;

    @GetMapping("/seats/schedule/{scheduleId}")
    public ResponseEntity<ApiResponse<List<SeatResponseDTO>>> getSeatsBySchedule(
            @PathVariable Long scheduleId) {

        List<SeatResponseDTO> seats =
                seatService.getSeatsByScheduleId(scheduleId);
        return ResponseEntity.ok(ApiResponse.success(seats));
    }
}
