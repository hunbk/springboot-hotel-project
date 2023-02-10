package com.ptbh.kyungsunghotel.domain.member;

import com.ptbh.kyungsunghotel.web.member.JoinForm;
import com.ptbh.kyungsunghotel.web.member.JoinResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public JoinResponse join(JoinForm joinForm) {
        Member member = Member.builder()
                .loginId(joinForm.getLoginId())
                .password(joinForm.getPassword())
                .name(joinForm.getName())
                .nickname(joinForm.getNickname())
                .email(joinForm.getEmail())
                .cellPhone(joinForm.getCellPhone())
                .build();
        Member savedMember = memberRepository.save(member);
        return JoinResponse.from(savedMember);
    }

    public boolean existsLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    public boolean existsNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
