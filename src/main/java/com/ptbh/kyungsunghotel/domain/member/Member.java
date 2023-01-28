
package com.ptbh.kyungsunghotel.domain.member;

import com.ptbh.kyungsunghotel.domain.board.Board;
import com.ptbh.kyungsunghotel.domain.reserve.Reserve;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String loginId;

    @Column(nullable = false, length = 30, unique = true)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 12, unique = true)
    private String nickname;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 11)
    private String cellPhone;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Reserve> reserves = new ArrayList<>();

    public Member(String loginId, String password, String name, String nickname, String email, String cellPhone) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.cellPhone = cellPhone;
    }

    public Member(Long id, String loginId, String password, String name, String nickname, String email, String cellPhone) {
        this(loginId, password, name, nickname, email, cellPhone);
        this.id = id;
    }

    public void update(String name, String nickname, String email, String cellPhone) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.cellPhone = cellPhone;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
