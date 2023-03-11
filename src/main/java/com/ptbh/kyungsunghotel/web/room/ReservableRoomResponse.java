package com.ptbh.kyungsunghotel.web.room;

import com.ptbh.kyungsunghotel.domain.room.RoomType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode
public class ReservableRoomResponse {

    private RoomType roomType;
    private String roomTypeName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int personnel;
    private String photoUrl;
    private int price;

    private ReservableRoomResponse(RoomType roomType,
                                   String roomTypeName,
                                   LocalDate checkIn,
                                   LocalDate checkOut,
                                   int personnel,
                                   String photoUrl,
                                   int price) {
        this.roomType = roomType;
        this.roomTypeName = roomTypeName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.personnel = personnel;
        this.photoUrl = photoUrl;
        this.price = price;
    }

    public static ReservableRoomResponse of(RoomType roomType,
                                            String roomTypeName,
                                            LocalDate checkIn,
                                            LocalDate checkOut,
                                            int personnel,
                                            String photoUrl,
                                            int price) {
        return new ReservableRoomResponse(roomType,
                roomTypeName,
                checkIn,
                checkOut,
                personnel,
                photoUrl,
                price);
    }
}
