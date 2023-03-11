package com.ptbh.kyungsunghotel.exception.reservation;

public class InvalidReservationPeriodException extends RuntimeException {
    private static final String MESSAGE = "체크인 날짜는 체크아웃 날짜와 같거나 늦을 수 없습니다.";

    public InvalidReservationPeriodException() {
        super(MESSAGE);
    }
}
