package com.app.vasyBus.controller.admin;

import com.app.vasyBus.dto.booking.BookingDetailResponse;
import com.app.vasyBus.dto.booking.BookingResponseDTO;
import com.app.vasyBus.service.booking.BookingService;
import com.app.vasyBus.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminBookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getAllBookings() {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getAllBookings()));
     }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingDetailResponse>> getBookingById(@PathVariable Long bookingId) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getBookingByIdForAdmin(bookingId)));
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getBookingsBySchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getBookingsBySchedule(scheduleId)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getBookingsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getBookingsByStatus(status)));
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelBooking(@PathVariable Long bookingId,
                                                             @RequestParam(required = false, defaultValue = "Cancelled by admin") String reason) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.cancelBookingByAdmin(bookingId, reason)));
    }
}