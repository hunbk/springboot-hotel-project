package com.ptbh.kyungsunghotel.web.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordForm {
    @NotBlank(message = "현재 비밀번호는 필수입니다")
    private String oldPassword;

    @NotBlank(message = "새 비밀번호는 필수입니다")
    private String newPassword;

    @NotBlank(message = "새 비밀번호 확인은 필수입니다")
    private String checkNewPassword;

    public PasswordForm() {
    }
}