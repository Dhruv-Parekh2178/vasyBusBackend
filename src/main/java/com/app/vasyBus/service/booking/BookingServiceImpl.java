package com.app.vasyBus.service.booking;

import com.app.vasyBus.dto.booking.BookingDetailResponse;
import com.app.vasyBus.dto.booking.BookingRequestDTO;
import com.app.vasyBus.dto.booking.BookingResponseDTO;
import com.app.vasyBus.dto.bookingSeat.BookingSeatRequestDTO;
import com.app.vasyBus.dto.bookingSeat.BookingSeatResponseDTO;
import com.app.vasyBus.enums.BookingStatus;
import com.app.vasyBus.enums.PaymentStatus;
import com.app.vasyBus.enums.ScheduleStatus;
import com.app.vasyBus.exception.ResourceNotFoundException;
import com.app.vasyBus.kafka.event.BookingCancelledEvent;
import com.app.vasyBus.kafka.event.BookingCreatedEvent;
import com.app.vasyBus.kafka.producer.BookingCancellationProducer;
import com.app.vasyBus.kafka.producer.BookingEventProducer;
import com.app.vasyBus.model.*;
import com.app.vasyBus.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService{

    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final RedisTemplate<String , Object> redisTemplate;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final BookingEventProducer bookingEventProducer;
    private final BookingCancellationProducer bookingCancellationProducer;

    private static final String SEAT_LOCK_PREFIX = "seat:lock:";

    @Override
    public BookingDetailResponse createBooking(BookingRequestDTO bookingRequestDTO, Long userId) {
        Schedule schedule = scheduleRepository.findById(bookingRequestDTO.getScheduleId())
                                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id :"+bookingRequestDTO.getScheduleId()));

        if(schedule.isDeleted() || schedule.getScheduleStatus() != ScheduleStatus.ACTIVE){
            throw new IllegalStateException("Schedule is not available for booking.");
        }

        if(bookingRequestDTO.getSeatIds().size() != bookingRequestDTO.getPassengers().size()){
            throw new IllegalStateException("Number of passenger must match number of seat selected");
        }

        Map<Long, BookingSeatRequestDTO> passengerMap = bookingRequestDTO.getPassengers()
                .stream()
                .collect(Collectors.toMap(
                        passenger -> passenger.getSeatId(),
                        passenger -> passenger
                ));

        List<Seat> seatsToBook =new ArrayList<>();

        for(Long seatId : bookingRequestDTO.getSeatIds()){
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found with Id :"+seatId));

            if(seat.isDeleted()){
                throw new ResourceNotFoundException("Seat not found with Id :"+seatId);
            }

            if(seat.isBooked()){
                throw new IllegalStateException("Seat "+ seat.getSeatNumber()+" is already booked.");
            }

            if(!seat.getSchedule().getScheduleId().equals(bookingRequestDTO.getScheduleId())){
                throw new IllegalStateException(
                        "Seat " + seat.getSeatNumber()
                                + " does not belong to this schedule");
            }
            String lockKey = SEAT_LOCK_PREFIX + seatId;
            Object lock = redisTemplate.opsForValue().get(lockKey);

            if(lock == null){
                throw new IllegalStateException("Seat "+ seat.getSeatNumber() + " is not locked. Please select and lock the seat before booking.");
            }

         Long  redisUserId =Long.parseLong(lock.toString());

            if (!redisUserId.equals(userId)) {
                throw new IllegalStateException("Seat " + seat.getSeatNumber() + " is locked by another user");
            }

            if (!passengerMap.containsKey(seatId)) {
                throw new IllegalStateException(
                        "Passenger details missing for seat "
                                + seat.getSeatNumber());
            }

            seatsToBook.add(seat);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id : "+userId));

        BigDecimal totalAmount = schedule.getPricePerSeat().multiply(BigDecimal.valueOf(bookingRequestDTO.getSeatIds().size()));

        Booking booking = Booking.builder()
                .user(user)
                .schedule(schedule)
                .totalAmount(totalAmount)
                .bookingStatus(BookingStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        List<BookingSeat> bookingSeats = new ArrayList<>();
        List<BookingCreatedEvent.PassengerInfo> passengerInfoList = new ArrayList<>();

        for(Seat seat : seatsToBook){
            BookingSeatRequestDTO passenger = passengerMap.get(seat.getSeatId());

            bookingSeats.add(BookingSeat.builder()
                    .booking(savedBooking)
                    .seat(seat)
                    .passengerName(passenger.getPassengerName())
                    .passengerAge(passenger.getPassengerAge())
                    .passengerGender(passenger.getPassengerGender())
                    .build());

            passengerInfoList.add(BookingCreatedEvent.PassengerInfo.builder()
                    .passengerName(passenger.getPassengerName())
                    .passengerAge(passenger.getPassengerAge())
                    .passengerGender(passenger.getPassengerGender().name())
                    .seatNumber(seat.getSeatNumber())
                    .seatType(seat.getSeatType().name())
                    .build());

            seatRepository.markSeatAsBooked(seat.getSeatId());

            redisTemplate.delete(SEAT_LOCK_PREFIX + seat.getSeatId());
        }

        bookingSeatRepository.saveAll(bookingSeats);

        try {
            bookingEventProducer.sendBookingCreatedEvent(
                    BookingCreatedEvent.builder()
                            .bookingId(savedBooking.getBookingId())
                            .userId(user.getUserId())
                            .userEmail(user.getEmail())
                            .userName(user.getName())
                            .busName(schedule.getBus().getBusName())
                            .busType(schedule.getBus().getBusType().name())
                            .sourceCity(schedule.getRoute().getSourceCity())
                            .destinationCity(schedule.getRoute().getDestinationCity())
                            .travelDate(schedule.getTravelDate())
                            .departureTime(schedule.getDepartureTime())
                            .arrivalTime(schedule.getArrivalTime())
                            .totalAmount(totalAmount)
                            .passengers(passengerInfoList)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to publish booking-created event: {}", e.getMessage());
        }

        return buildBookingDetail(savedBooking.getBookingId());

    }

    @Override
    public BookingDetailResponse getBookingById(Long bookingId, Long userId) {
          BookingResponseDTO booking = bookingRepository.findBookingById(bookingId);

          if(booking == null){
              throw new ResourceNotFoundException("Booking not found with id : "+bookingId);
          }

        if (!booking.getUserId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to view this booking");
              }

          List<BookingSeatResponseDTO> passengers = bookingSeatRepository.findAllPassengersByBookingId(bookingId);

          return new BookingDetailResponse(booking , passengers);
    }

    @Override
    public List<BookingResponseDTO> getMyBookings(Long userId) {
        return bookingRepository.findBookingsByUserId(userId);
    }

    @Override
    @Transactional
    public String cancelBooking(Long bookingId, String reason, Long userId) {

        BookingResponseDTO booking =
                bookingRepository.findBookingById(bookingId);
        if (booking == null) {
            throw new ResourceNotFoundException(
                    "Booking not found with id: " + bookingId);
        }
        if (!booking.getUserId().equals(userId)) {
            throw new IllegalStateException(
                    "You are not authorized to cancel this booking");
        }
        if ("CANCELLED".equals(booking.getBookingStatus())) {
            throw new IllegalStateException(
                    "Booking is already cancelled");
        }
        List<BookingSeatResponseDTO> passengers =
                bookingSeatRepository.findAllPassengersByBookingId(bookingId);
        passengers.forEach(p ->
                seatRepository.markSeatAsAvailable(p.getSeatId()));
        bookingSeatRepository.hardDeleteByBookingId(bookingId);
        bookingRepository.cancelBooking(bookingId, "USER", reason);

        try {
            String userEmail = userRepository.findById(booking.getUserId())
                    .map(u -> u.getEmail())
                    .orElse("");
            bookingCancellationProducer.sendBookingCancelledEvent(
                    BookingCancelledEvent.builder()
                            .bookingId(bookingId)
                            .userId(booking.getUserId())
                            .userEmail(userEmail)
                            .userName(booking.getUserName())
                            .busName(booking.getBusName())
                            .sourceCity(booking.getSourceCity())
                            .destinationCity(booking.getDestinationCity())
                            .travelDate(booking.getTravelDate())
                            .departureTime(booking.getDepartureTime())
                            .totalAmount(booking.getTotalAmount())
                            .cancelledBy("USER")
                            .cancellationReason(reason)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to publish booking-cancelled event: {}", e.getMessage());
        }

        return "Booking cancelled successfully";
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAllBookings();
    }

    @Override
    public List<BookingResponseDTO> getBookingsBySchedule(Long scheduleId) {
        return bookingRepository.findBookingsByScheduleId(scheduleId);
    }

    @Override
    public List<BookingResponseDTO> getBookingsByStatus(String status) {
        return bookingRepository.findBookingsByStatus(status);
    }

    @Override
    public BookingDetailResponse getBookingByIdForAdmin(Long bookingId) {
        return buildBookingDetail(bookingId);
    }

    @Override
    @Transactional
    public String cancelBookingByAdmin(Long bookingId, String reason) {

        BookingResponseDTO booking =
                bookingRepository.findBookingById(bookingId);
        if (booking == null) {
            throw new ResourceNotFoundException(
                    "Booking not found with id: " + bookingId);
        }
        if ("CANCELLED".equals(booking.getBookingStatus())) {
            throw new IllegalStateException(
                    "Booking is already cancelled");
        }

        List<BookingSeatResponseDTO> passengers =
                bookingSeatRepository.findAllPassengersByBookingId(bookingId);
        passengers.forEach(p ->
                seatRepository.markSeatAsAvailable(p.getSeatId()));
        bookingSeatRepository.softDeleteByBookingId(bookingId);
        bookingRepository.cancelBooking(bookingId, "ADMIN", reason);

        try {
            String userEmail = userRepository.findById(booking.getUserId())
                    .map(u -> u.getEmail())
                    .orElse("");
            bookingCancellationProducer.sendBookingCancelledEvent(
                    BookingCancelledEvent.builder()
                            .bookingId(bookingId)
                            .userId(booking.getUserId())
                            .userEmail(userEmail)
                            .userName(booking.getUserName())
                            .busName(booking.getBusName())
                            .sourceCity(booking.getSourceCity())
                            .destinationCity(booking.getDestinationCity())
                            .travelDate(booking.getTravelDate())
                            .departureTime(booking.getDepartureTime())
                            .totalAmount(booking.getTotalAmount())
                            .cancelledBy("ADMIN")
                            .cancellationReason(reason)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to publish booking-cancelled event (admin): {}", e.getMessage());
        }

        return "Booking cancelled by admin successfully";
    }

    private BookingDetailResponse buildBookingDetail(Long bookingId) {
        BookingResponseDTO booking =
                bookingRepository.findBookingById(bookingId);

        if (booking == null) {
            throw new ResourceNotFoundException(
                    "Booking not found with id: " + bookingId);
        }

        List<BookingSeatResponseDTO> passengers =
                bookingSeatRepository.findAllPassengersByBookingId(bookingId);

        return new BookingDetailResponse(booking, passengers);
    }
}

