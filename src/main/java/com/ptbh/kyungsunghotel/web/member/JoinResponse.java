package com.ptbh.kyungsunghotel.web.member;

import com.ptbh.kyungsunghotel.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinResponse {

    private Long id;
    private String nickname;

    public JoinResponse(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public static JoinResponse from(Member member) {
        return new JoinResponse(member.getId(), member.getNickname());
    }
}
