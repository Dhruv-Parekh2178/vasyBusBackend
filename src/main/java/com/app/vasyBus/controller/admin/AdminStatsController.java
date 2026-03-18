package com.app.vasyBus.controller.admin;

import com.app.vasyBus.dto.admin.DashboardStatsDTO;
import com.app.vasyBus.repository.AdminStatsRepository;
import com.app.vasyBus.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminStatsController {

    private final AdminStatsRepository adminStatsRepository;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDTO>> getDashboardStats() {

        DashboardStatsDTO stats = DashboardStatsDTO.builder()
                .totalBuses(adminStatsRepository.countActiveBuses())
                .totalRoutes(adminStatsRepository.countActiveRoutes())
                .totalSchedules(adminStatsRepository.countActiveSchedules())
                .totalBookings(adminStatsRepository.countTotalBookings())
                .confirmedBookings(adminStatsRepository.countBookingsByStatus("CONFIRMED"))
                .pendingBookings(adminStatsRepository.countBookingsByStatus("PENDING"))
                .cancelledBookings(adminStatsRepository.countBookingsByStatus("CANCELLED"))
                .totalUsers(adminStatsRepository.countTotalUsers())
                .revenueToday(adminStatsRepository.revenueToday())
                .revenueTotal(adminStatsRepository.revenueTotal())
                .bookingsToday(adminStatsRepository.countBookingsToday())
                .build();

        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}