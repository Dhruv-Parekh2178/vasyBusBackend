package com.app.vasyBus.service.booking;

import com.app.vasyBus.dto.booking.BookingDetailResponse;
import com.app.vasyBus.dto.booking.BookingRequestDTO;
import com.app.vasyBus.dto.booking.BookingResponseDTO;

import java.util.List;

public interface BookingService {

    //below method are for authenticated user only
    BookingDetailResponse createBooking(BookingRequestDTO bookingRequestDTO , Long userId);
    BookingDetailResponse getBookingById(Long bookingId , Long userId);
    List<BookingResponseDTO> getMyBookings(Long userId);
    String cancelBooking(Long bookingId , String reason,Long userId);

    //below method are for admin only
    List<BookingResponseDTO> getAllBookings();
    List<BookingResponseDTO> getBookingsBySchedule(Long scheduleId);
    List<BookingResponseDTO> getBookingsByStatus(String status);
    BookingDetailResponse getBookingByIdForAdmin(Long bookingId);
    String cancelBookingByAdmin(Long bookingId , String reason);
}
