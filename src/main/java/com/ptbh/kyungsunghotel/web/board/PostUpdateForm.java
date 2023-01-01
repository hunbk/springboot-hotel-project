package com.ptbh.kyungsunghotel.web.board;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class PostUpdateForm {

    private Integer boardNo;

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    private String content;

    private LocalDateTime createTime;

    public PostUpdateForm() {
    }
}
