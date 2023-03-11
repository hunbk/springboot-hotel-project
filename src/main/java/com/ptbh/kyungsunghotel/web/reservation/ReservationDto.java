package com.ptbh.kyungsunghotel.web.reservation;

import com.ptbh.kyungsunghotel.domain.reservation.Reservation;
import com.ptbh.kyungsunghotel.domain.room.RoomType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationDto {

    private Long id;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private RoomType roomType;

    private Integer personnel;

    public ReservationDto(Long id, LocalDate checkIn, LocalDate checkOut, RoomType roomType, Integer personnel) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomType = roomType;
        this.personnel = personnel;
    }

    public static ReservationDto from(Reservation reservation) {
        return new ReservationDto(reservation.getId(), reservation.getCheckIn(), reservation.getCheckOut(), reservation.getRoom().getRoomType(), reservation.getPersonnel());
    }
}
