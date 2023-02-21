package com.ptbh.kyungsunghotel.domain.member;

import com.ptbh.kyungsunghotel.web.member.ChangePasswordForm;
import com.ptbh.kyungsunghotel.web.member.JoinForm;
import com.ptbh.kyungsunghotel.web.member.JoinResponse;
import com.ptbh.kyungsunghotel.web.member.MemberUpdateForm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ptbh.kyungsunghotel.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void 회원가입_요청시_회원을_생성한다() {
        //given
        Member member = member();
        JoinForm joinForm = new JoinForm(LOGIN_ID, PASSWORD, PASSWORD, NAME, NICKNAME, EMAIL, CELLPHONE);
        given(memberRepository.save(any(Member.class)))
                .willReturn(member);

        //then
        JoinResponse joinResponse = memberService.join(joinForm);

        //when
        assertThat(joinResponse).isNotNull();
        assertThat(joinResponse.getId()).isEqualTo(1L);
        assertThat(joinResponse.getNickname()).isEqualTo(NICKNAME);
    }

    @Test
    void 회원수정_요청시_회원을_수정한다() {
        //given
        Member member = member();
        MemberUpdateForm memberUpdateForm = new MemberUpdateForm("장영실", "jang", "jang@email.com", "01022222222");
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(member));

        //when
        memberService.updateMember(member.getId(), memberUpdateForm);

        //then
        assertThat(member.getName()).isEqualTo(memberUpdateForm.getName());
        assertThat(member.getNickname()).isEqualTo(memberUpdateForm.getNickname());
        assertThat(member.getEmail()).isEqualTo(memberUpdateForm.getEmail());
        assertThat(member.getCellPhone()).isEqualTo(memberUpdateForm.getCellPhone());
    }

    @Test
    void 비밀번호_변경요청시_비밀번호가_변경된다() {
        //given
        Member member = member();
        ChangePasswordForm changePasswordForm = new ChangePasswordForm(PASSWORD, "jang1234!", "jang1234!");
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(member));

        //when
        memberService.changePassword(member.getId(), changePasswordForm);

        //then
        assertThat(member.getPassword()).isEqualTo(changePasswordForm.getNewPassword());
    }

    @Test
    void 회원삭제_요청시_회원이_삭제된다() {
        //given
        Member member = member();
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(member));

        //when, then
        assertThatCode(() -> memberService.deleteByMemberId(member.getId())).doesNotThrowAnyException();
    }

    public Member member() {
        return new Member(1L, LOGIN_ID, PASSWORD, NAME, NICKNAME, EMAIL, CELLPHONE);
    }
}
