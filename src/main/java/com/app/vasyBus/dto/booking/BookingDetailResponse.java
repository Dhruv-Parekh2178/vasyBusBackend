package com.app.vasyBus.dto.booking;

import com.app.vasyBus.dto.bookingSeat.BookingSeatResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookingDetailResponse {
    private BookingResponseDTO booking;
    private List<BookingSeatResponseDTO> passengers;
}