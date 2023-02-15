package com.ptbh.kyungsunghotel.domain.member;

import lombok.Getter;

@Getter
public class MemberDto {

    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String cellPhone;

    public MemberDto(Long id, String name, String nickname, String email, String cellPhone) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.cellPhone = cellPhone;
    }

    public static MemberDto from(Member foundMember) {
        return new MemberDto(
                foundMember.getId(),
                foundMember.getName(),
                foundMember.getNickname(),
                foundMember.getEmail(),
                foundMember.getCellPhone()
        );
    }
}
