package com.ptbh.kyungsunghotel.web.room;

import com.ptbh.kyungsunghotel.domain.room.RoomService;
import com.ptbh.kyungsunghotel.exception.reservation.InvalidReservationPeriodException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomApiController {

    private final RoomService roomService;

    @GetMapping("/reservable")
    public ResponseEntity<ReservableRoomSearchResponse> findReservableRoom(@Validated ReservableRoomSearchRequest reservableRoomSearchRequest) {
        LocalDate checkIn = reservableRoomSearchRequest.getCheckIn();
        LocalDate checkOut = reservableRoomSearchRequest.getCheckOut();
        if (checkIn.isEqual(checkOut) || checkIn.isAfter(checkIn)) {
            throw new InvalidReservationPeriodException();
        }
        ReservableRoomSearchResponse reservableRoomSearchResponse = roomService.findReservableRoom(reservableRoomSearchRequest);
        return ResponseEntity.ok().body(reservableRoomSearchResponse);
    }
}
