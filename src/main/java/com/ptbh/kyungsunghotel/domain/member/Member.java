
package com.ptbh.kyungsunghotel.domain.member;

import com.ptbh.kyungsunghotel.domain.board.Board;
import com.ptbh.kyungsunghotel.domain.reserve.Reserve;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Member {
    @Id
    @Column(name = "loginId")
    @NotBlank(message = "아이디는 필수입니다")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @NotBlank(message = "이메일은 필수입니다")
    private String email;

    @NotBlank(message = "전화번호는 필수입니다")
    private String telephone;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Reserve> reserves = new ArrayList<>();

    public Member() {
    }

    @Builder
    public Member(String loginId, String password, String name, String email, String telephone) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
    }
}
