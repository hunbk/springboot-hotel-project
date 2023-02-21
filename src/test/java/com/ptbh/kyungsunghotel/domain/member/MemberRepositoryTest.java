package com.ptbh.kyungsunghotel.domain.member;

import com.ptbh.kyungsunghotel.exception.member.NoSuchMemberException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.ptbh.kyungsunghotel.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원저장() {
        //given
        Member member = new Member(LOGIN_ID, PASSWORD, NAME, NICKNAME, EMAIL, CELLPHONE);

        //when
        Member savedMember = memberRepository.save(member);

        //then
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember).isEqualTo(member);
    }

    @Test
    void 로그인아이디로_회원조회() {
        //given
        Member member = new Member(LOGIN_ID, PASSWORD, NAME, NICKNAME, EMAIL, CELLPHONE);
        memberRepository.save(member);

        //when
        Member foundMember = memberRepository.findByLoginId(LOGIN_ID)
                .orElseThrow(NoSuchMemberException::new);

        //then
        assertThat(foundMember).isEqualTo(member);
    }

    @Test
    void 로그인아이디_중복검사() {
        //given
        Member member = new Member(LOGIN_ID, PASSWORD, NAME, NICKNAME, EMAIL, CELLPHONE);
        memberRepository.save(member);

        //when
        boolean hgd123 = memberRepository.existsByLoginId(LOGIN_ID);
        boolean wrong = memberRepository.existsByLoginId("wrong");

        //then
        assertThat(hgd123).isTrue();
        assertThat(wrong).isFalse();
    }

    @Test
    void 닉네임_중복검사() {
        //given
        Member member = new Member(LOGIN_ID, PASSWORD, NAME, NICKNAME, EMAIL, CELLPHONE);
        memberRepository.save(member);

        //when
        boolean hong = memberRepository.existsByNickname(NICKNAME);
        boolean wrong = memberRepository.existsByNickname("wrong");

        //then
        assertThat(hong).isTrue();
        assertThat(wrong).isFalse();
    }
}
