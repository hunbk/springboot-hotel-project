package com.ptbh.kyungsunghotel.domain.auth;

import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.member.MemberRepository;
import com.ptbh.kyungsunghotel.web.auth.LoginForm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ptbh.kyungsunghotel.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void 로그인_성공() {
        //given
        Member member = member();
        LoginForm loginForm = new LoginForm(LOGIN_ID, PASSWORD);

        given(memberRepository.findByLoginId(anyString()))
                .willReturn(Optional.ofNullable(member));

        //when
        Member loginMember = authService.login(loginForm);

        //then
        assertThat(loginMember).isNotNull();
        assertThat(loginMember.getLoginId()).isEqualTo(loginForm.getLoginId());
        assertThat(loginMember.getPassword()).isEqualTo(loginForm.getPassword());
    }

    @Test
    void 로그인_실패() {
        //given
        Member member = member();
        LoginForm loginForm = new LoginForm("qwer", "qwer");

        given(memberRepository.findByLoginId(anyString()))
                .willReturn(Optional.ofNullable(member));

        //when
        Member loginMember = authService.login(loginForm);

        //then
        assertThat(loginMember).isNull();
    }

    public Member member() {
        return Member.builder()
                .loginId(LOGIN_ID)
                .password(PASSWORD)
                .name(NAME)
                .nickname(NICKNAME)
                .email(EMAIL)
                .cellPhone(CELLPHONE)
                .build();
    }
}
