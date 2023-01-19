package com.ptbh.kyungsunghotel.exception.board;

public class NoSuchBoardException extends RuntimeException {
    private static final String MESSAGE = "존재하지 않는 게시글입니다.";

    public NoSuchBoardException() {
        super(MESSAGE);
    }
}
