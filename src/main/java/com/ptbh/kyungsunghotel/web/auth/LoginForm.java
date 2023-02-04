package com.ptbh.kyungsunghotel.web.auth;

import com.ptbh.kyungsunghotel.web.ValidationGroups;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@GroupSequence({
        LoginForm.class,
        ValidationGroups.Step1.class,
        ValidationGroups.Step2.class
})
public class LoginForm {

    @NotEmpty(groups = ValidationGroups.Step1.class, message = "아이디는 필수입니다.")
    private String loginId;

    @NotBlank(groups = ValidationGroups.Step2.class, message = "비밀번호는 필수입니다.")
    private String password;

    public LoginForm(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
