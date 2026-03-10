package com.app.vasyBus.service.seat;

import com.app.vasyBus.dto.seat.SeatLockRequestDTO;
import com.app.vasyBus.dto.seat.SeatResponseDTO;
import com.app.vasyBus.exception.ResourceNotFoundException;
import com.app.vasyBus.repository.ScheduleRepository;
import com.app.vasyBus.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService{

    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;
    private final RedisTemplate<String , Object> redisTemplate;

    private static final String SEAT_LOCK_PREFIX = "seat:lock:";
    private static final Duration SEAT_LOCK_TTL  = Duration.ofSeconds(600);

    @Override
    public List<SeatResponseDTO> getSeatsByScheduleId(Long scheduleId) {
        if(!scheduleRepository.existsActiveScheduleById(scheduleId)){
            throw new ResourceNotFoundException("Schedule not found with id :" + scheduleId);
        }

        return seatRepository.findSeatByScheduleId(scheduleId);
    }

    @Override
    public String lockSeat(SeatLockRequestDTO seatLockRequestDTO, Long userId) {
        SeatResponseDTO seat = seatRepository.findSeatBySeatId(seatLockRequestDTO.getSeatId());
        if(seat == null){
            throw new ResourceNotFoundException("Seat not found with id :"+ seatLockRequestDTO.getSeatId());
        }

        if(seat.getBooked()){
            throw new IllegalStateException("Seat" + seat.getSeatNumber() + "is already booked.");
        }

        String lockKey = SEAT_LOCK_PREFIX + seatLockRequestDTO.getSeatId();
        Object existingLock = redisTemplate.opsForValue().get(lockKey);

        if(existingLock != null){
            Long lockedByUserId = Long.parseLong(existingLock.toString());

            if (lockedByUserId.equals(userId)) {
                redisTemplate.expire(lockKey, Duration.ofSeconds(300));
                return "Seat lock refreshed for seat " + seat.getSeatNumber();
            }

            throw new IllegalStateException(
                    "Seat " + seat.getSeatNumber()
                            + " is temporarily locked by another user");
        }
        redisTemplate.opsForValue().set(lockKey, userId.toString(), SEAT_LOCK_TTL);
        return "Seat " + seat.getSeatNumber() + " locked successfully for 10 minutes";
    }

    @Override
    public String unlockSeat(Long seatId, Long userId) {
        String lockKey = SEAT_LOCK_PREFIX + seatId;
        Object existingLock = redisTemplate.opsForValue().get(lockKey);

        if (existingLock == null) {
            return "Seat is not locked";
        }

        Long lockedByUserId = Long.parseLong(existingLock.toString());

        if (!lockedByUserId.equals(userId)) {
            throw new IllegalStateException(
                    "You cannot unlock a seat locked by another user");
        }
        redisTemplate.delete(lockKey);
        return "Seat unlocked successfully";
    }
}
