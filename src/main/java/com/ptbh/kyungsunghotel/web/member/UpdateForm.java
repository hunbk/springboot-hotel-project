package com.ptbh.kyungsunghotel.web.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateForm {

    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @NotBlank(message = "닉네임은 필수입니다")
    private String nickname;

    @NotBlank(message = "이메일은 필수입니다")
    private String email;

    @NotBlank(message = "전화번호는 필수입니다")
    private String cellPhone;

    public UpdateForm() {
    }
}