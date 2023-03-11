package com.ptbh.kyungsunghotel.exception.order;

public class TotalPriceMismatchException extends RuntimeException {
    private static final String MESSAGE = "주문 가격이 올바르지 않습니다.";

    public TotalPriceMismatchException() {
        super(MESSAGE);
    }
}
