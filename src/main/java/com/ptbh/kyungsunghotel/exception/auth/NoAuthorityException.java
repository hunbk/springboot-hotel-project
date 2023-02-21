package com.ptbh.kyungsunghotel.exception.auth;

public class NoAuthorityException extends RuntimeException {
    private static final String MESSAGE = "잘못된 접근입니다.";

    public NoAuthorityException() {
        super(MESSAGE);
    }
}
