package com.ptbh.kyungsunghotel.web.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class JoinForm {
    @NotBlank(message = "아이디는 필수입니다")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @NotBlank(message = "이메일은 필수입니다")
    private String email;

    @NotBlank(message = "전화번호는 필수입니다")
    private String telephone;

    public JoinForm() {
    }
}
