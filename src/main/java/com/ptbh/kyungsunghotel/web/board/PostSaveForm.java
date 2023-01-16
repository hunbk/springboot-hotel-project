package com.ptbh.kyungsunghotel.web.board;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PostSaveForm {

    @NotBlank(message = "제목은 공백일 수 없습니다.")
    @Size(max = 50, message = "50자를 초과할 수 없습니다.")
    private String title;

    @NotBlank(message = "내용은 공백일 수 없습니다.")
    private String content;

}
