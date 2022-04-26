package com.ptbh.kyungsunghotel.web.board;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostViewForm {

    private Integer boardNo;

    private String writer;

    private String loginId;

    private String title;

    private String content;

    private LocalDateTime createTime;

    public PostViewForm() {
    }
}
