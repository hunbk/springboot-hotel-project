package com.ptbh.kyungsunghotel.domain.auth;

import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.member.MemberRepository;
import com.ptbh.kyungsunghotel.web.auth.LoginForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;

    public Member login(LoginForm loginForm) {
        return memberRepository.findByLoginId(loginForm.getLoginId())
                .filter(m -> m.getPassword().equals(loginForm.getPassword()))
                .orElse(null);
    }

}
