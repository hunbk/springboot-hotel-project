package com.ptbh.kyungsunghotel.web.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordForm {

    @NotBlank(message = "현재 비밀번호는 필수입니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?!.*[\\sㄱ-힣])(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!#$%&'()*+,./:;<=>?@₩^_`{|}~\"\\[\\]\\-]).{8,30}$",
            message = "8~30자의 영문 대 소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String newPassword;

    @NotBlank(message = "새 비밀번호 재확인은 필수입니다.")
    private String newPasswordCheck;

    public ChangePasswordForm(String currentPassword, String newPassword, String newPasswordCheck) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newPasswordCheck = newPasswordCheck;
    }
}
