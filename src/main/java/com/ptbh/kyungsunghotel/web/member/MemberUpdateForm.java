package com.ptbh.kyungsunghotel.web.member;

import com.ptbh.kyungsunghotel.domain.member.MemberDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MemberUpdateForm {

    @NotBlank(message = "이름은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣]{1,10}$",
            message = "한글과 영어 대 소문자만 사용 가능합니다. (특수문자, 공백 사용 불가)")
    private String name;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^[\\da-zA-Zㄱ-힣]{2,12}$",
            message = "2~12자의 한글과 영어 대 소문자, 숫자만 사용 가능합니다. (특수문자, 공백 사용 불가)")
    private String nickname;

    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^(?!.*(\\.\\.|\\\\\\\\|\\s)).+@[\\da-zA-Z.\\-]+\\.[\\da-zA-Z]+$",
            message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^01[016-9]\\d{3,4}\\d{4}$",
            message = "잘못된 형식의 전화번호입니다.")
    private String cellPhone;

    public MemberUpdateForm(String name, String nickname, String email, String cellPhone) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.cellPhone = cellPhone;
    }

    public static MemberUpdateForm from(MemberDto memberDto) {
        return new MemberUpdateForm(
                memberDto.getName(),
                memberDto.getNickname(),
                memberDto.getEmail(),
                memberDto.getCellPhone()
        );
    }
}
