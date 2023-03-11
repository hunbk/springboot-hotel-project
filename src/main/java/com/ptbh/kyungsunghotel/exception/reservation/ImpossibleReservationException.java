package com.ptbh.kyungsunghotel.exception.reservation;

public class ImpossibleReservationException extends RuntimeException {
    private static final String MESSAGE = "현재 선택하신 방은 예약하실 수 없습니다.";

    public ImpossibleReservationException() {
        super(MESSAGE);
    }
}
