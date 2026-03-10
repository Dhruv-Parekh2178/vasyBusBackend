package com.app.vasyBus.controller.authUser;

import com.app.vasyBus.config.security.JwtUtil;
import com.app.vasyBus.dto.seat.SeatLockRequestDTO;
import com.app.vasyBus.dto.seat.SeatResponseDTO;
import com.app.vasyBus.service.seat.SeatService;
import com.app.vasyBus.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SeatController {

    private final SeatService seatService;
    private final JwtUtil jwtUtil;

    @PostMapping("/seats/lock")
    public ResponseEntity<ApiResponse<String>> lockSeat(
            @Valid @RequestBody SeatLockRequestDTO seatLockRequestDTO,
            HttpServletRequest request) {

        Long userId = extractUserId(request);
        String result = seatService.lockSeat(seatLockRequestDTO, userId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/seats/unlock")
    public ResponseEntity<ApiResponse<String>> unlockSeat(
            @RequestParam Long seatId,
            HttpServletRequest request) {

        Long userId = extractUserId(request);
        String result = seatService.unlockSeat(seatId, userId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    private Long extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }
}