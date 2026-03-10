package com.app.vasyBus.dto.bookingSeat;

public interface BookingSeatResponseDTO {
    Long getBookingSeatId();
    Long getBookingId();
    Long getSeatId();
    String getSeatNumber();
    String getSeatType();
    String getPassengerName();
    Integer getPassengerAge();
    String getPassengerGender();
}