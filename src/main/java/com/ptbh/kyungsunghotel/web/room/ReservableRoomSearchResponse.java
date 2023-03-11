package com.ptbh.kyungsunghotel.web.room;

import lombok.Getter;

import java.util.List;

@Getter
public class ReservableRoomSearchResponse {

    private List<ReservableRoomResponse> data;

    private ReservableRoomSearchResponse(List<ReservableRoomResponse> reservableRoomResponses) {
        this.data = reservableRoomResponses;
    }

    public static ReservableRoomSearchResponse of(List<ReservableRoomResponse> data) {
        return new ReservableRoomSearchResponse(data);
    }
}
