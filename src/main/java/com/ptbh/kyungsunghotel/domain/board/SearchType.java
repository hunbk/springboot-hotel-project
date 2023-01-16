package com.ptbh.kyungsunghotel.domain.board;

public enum SearchType {
    T("제목"), TC("제목 + 내용"), W("작성자");

    private final String description;

    SearchType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
