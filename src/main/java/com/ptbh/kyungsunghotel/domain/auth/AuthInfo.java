package com.ptbh.kyungsunghotel.domain.auth;

import com.ptbh.kyungsunghotel.domain.member.Member;
import lombok.Getter;

@Getter
public class AuthInfo {

    private Long id;
    private String nickname;

    public AuthInfo(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public static AuthInfo from(Member member) {
        return new AuthInfo(member.getId(), member.getNickname());
    }
}
