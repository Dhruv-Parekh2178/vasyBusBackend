package com.app.vasyBus.controller.authuser;

import com.app.vasyBus.config.security.JwtUtil;
import com.app.vasyBus.dto.booking.BookingDetailResponse;
import com.app.vasyBus.dto.booking.BookingRequestDTO;
import com.app.vasyBus.dto.booking.BookingResponseDTO;
import com.app.vasyBus.service.booking.BookingService;
import com.app.vasyBus.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final JwtUtil jwtUtil;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingDetailResponse>> createBooking(@Valid @RequestBody BookingRequestDTO bookingRequestDTO , HttpServletRequest request){
        Long userId = extractUserId(request);
        BookingDetailResponse response = bookingService.createBooking(bookingRequestDTO , userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingDetailResponse>> getBookingsById(@PathVariable Long bookingId,HttpServletRequest request){
        Long userId = extractUserId(request);
        BookingDetailResponse response = bookingService.getBookingById(bookingId , userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getMyBookings(HttpServletRequest request){
        Long userId = extractUserId(request);
        List<BookingResponseDTO> responseDTOS = bookingService.getMyBookings(userId);
        return ResponseEntity.ok(ApiResponse.success(responseDTOS));
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelBooking(@PathVariable Long bookingId ,
                                                             @RequestParam(required = false, defaultValue = "Cancelled by user") String reason, HttpServletRequest request){
        Long userId = extractUserId(request);
         String response = bookingService.cancelBooking(bookingId ,reason ,userId);
         return ResponseEntity.ok(ApiResponse.success(response));
    }

    private Long extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }
}
