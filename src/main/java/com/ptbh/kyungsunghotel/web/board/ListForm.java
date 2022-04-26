package com.ptbh.kyungsunghotel.web.board;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ListForm {

    private String boardNo;

    private String writer;

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    private LocalDateTime createTime;

    public ListForm() {
    }

}
