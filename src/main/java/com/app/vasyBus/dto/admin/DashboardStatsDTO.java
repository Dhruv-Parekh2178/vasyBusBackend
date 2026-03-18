package com.app.vasyBus.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalBuses;
    private Long totalRoutes;
    private Long totalSchedules;
    private Long totalBookings;
    private Long confirmedBookings;
    private Long pendingBookings;
    private Long cancelledBookings;
    private Long totalUsers;
    private BigDecimal revenueToday;
    private BigDecimal revenueTotal;
    private Long bookingsToday;
}