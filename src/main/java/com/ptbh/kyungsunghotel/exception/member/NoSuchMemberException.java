package com.ptbh.kyungsunghotel.exception.member;

public class NoSuchMemberException extends RuntimeException {
    private static final String MESSAGE = "존재하지 않는 회원입니다.";

    public NoSuchMemberException() {
        super(MESSAGE);
    }
}
