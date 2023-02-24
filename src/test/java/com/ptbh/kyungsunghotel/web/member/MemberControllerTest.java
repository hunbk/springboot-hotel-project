package com.ptbh.kyungsunghotel.web.member;

import com.ptbh.kyungsunghotel.DatabaseCleaner;
import com.ptbh.kyungsunghotel.domain.auth.AuthInfo;
import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.member.MemberRepository;
import com.ptbh.kyungsunghotel.web.SessionConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.ptbh.kyungsunghotel.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;

    private AuthInfo authInfo;

    @BeforeEach
    void setup() {
        Member member = new Member(LOGIN_ID, PASSWORD, NAME, NICKNAME, EMAIL, CELLPHONE);
        memberRepository.save(member);
        authInfo = new AuthInfo(member.getId(), member.getNickname());
    }

    @AfterEach
    void clear() {
        databaseCleaner.execute();
    }

    @Test
    void 회원가입_성공() throws Exception {
        mockMvc.perform(post("/join")
                        .param("loginId", "ks1234")
                        .param("password", "ks1234!@")
                        .param("passwordCheck", "ks1234!@")
                        .param("name", "경성")
                        .param("nickname", "ks")
                        .param("email", "ks@email.com")
                        .param("cellPhone", "01011111111"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attributeExists("joinResponse"))
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void 회원가입_유효성_검증_실패() throws Exception {
        mockMvc.perform(post("/join")
                        .param("loginId", "ks") //5~20자 위반
                        .param("password", "ks1234") //특수문자 없음
                        .param("passwordCheck", "ks1") //비밀번호 재확인 틀림
                        .param("name", "!") //특수문자 위반
                        .param("nickname", "!") //특수문자 위반
                        .param("email", "ks.com") //올바르지 않은 이메일
                        .param("cellPhone", "123456789")) //올바르지 않은 전화번호
                .andDo(print())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("joinForm", "loginId", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("joinForm", "password", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("joinForm", "passwordCheck", "notSame"))
                .andExpect(model().attributeHasFieldErrorCode("joinForm", "name", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("joinForm", "nickname", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("joinForm", "email", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("joinForm", "cellPhone", "Pattern"));
    }

    @Test
    void 회원가입_UNIQUE_제약조건_위반() throws Exception {
        mockMvc.perform(post("/join")
                        .param("loginId", LOGIN_ID)
                        .param("password", PASSWORD)
                        .param("passwordCheck", PASSWORD)
                        .param("name", NAME)
                        .param("nickname", NICKNAME)
                        .param("email", EMAIL)
                        .param("cellPhone", CELLPHONE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("joinForm", "loginId", "exists"))
                .andExpect(model().attributeHasFieldErrorCode("joinForm", "nickname", "exists"));
    }

    @Test
    void 회원_프로필() throws Exception {
        mockMvc.perform(get("/members/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("memberId", "nickname", "boardPage"));
    }

    @Test
    void 로그인시_내_정보를_볼_수_있다() throws Exception {
        mockMvc.perform(get("/account")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("memberDto", "reserves"));
    }

    @Test
    void 비로그인시_내_정보를_볼_수_없다() throws Exception {
        mockMvc.perform(get("/account"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?redirectURL=/account"));
    }

    @Test
    void 회원정보_수정_성공() throws Exception {
        mockMvc.perform(post("/account/update")
                        .param("name", "장영실")
                        .param("nickname", "jang")
                        .param("email", "jang@email.com")
                        .param("cellPhone", "01012341234")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account"));
    }

    @Test
    void 회원정보_수정_실패_비로그인() throws Exception {
        mockMvc.perform(post("/account/update")
                        .param("name", "장영실")
                        .param("nickname", "jang")
                        .param("email", "jang@email.com")
                        .param("cellPhone", "01012341234"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?redirectURL=/account/update"));
    }

    @Test
    void 회원정보_수정_유효성검증_실패() throws Exception {
        mockMvc.perform(post("/account/update")
                        .param("name", "!")
                        .param("nickname", "!")
                        .param("email", "jang@.com")
                        .param("cellPhone", "12341234")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("memberUpdateForm", "name", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("memberUpdateForm", "nickname", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("memberUpdateForm", "email", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("memberUpdateForm", "cellPhone", "Pattern"));
    }

    @Test
    void 회원정보_수정_UNIQUE_제약조건_위반() throws Exception {
        Member member2 = new Member("member2", "member2!", "member2", "member2", "member2@email.com", "01022222222");
        memberRepository.save(member2);

        mockMvc.perform(post("/account/update")
                        .param("name", "jang")
                        .param("nickname", member2.getNickname()) //중복된 닉네임
                        .param("email", "jang@email.com")
                        .param("cellPhone", "01012341234")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("memberUpdateForm", "nickname", "exists"));
    }

    @Test
    void 비밀번호_변경_성공() throws Exception {
        mockMvc.perform(post("/account/change-password")
                        .param("currentPassword", PASSWORD)
                        .param("newPassword", "password12!@")
                        .param("newPasswordCheck", "password12!@")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account"));
    }

    @Test
    void 비밀번호_변경_실패_비로그인() throws Exception {
        mockMvc.perform(post("/account/change-password")
                        .param("currentPassword", PASSWORD)
                        .param("newPassword", "password12!@")
                        .param("newPasswordCheck", "password12!@"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?redirectURL=/account/change-password"));
    }

    @Test
    void 비밀번호_변경_유효성검증_실패() throws Exception {
        mockMvc.perform(post("/account/change-password")
                        .param("currentPassword", "")
                        .param("newPassword", "password") //특수문자 없음
                        .param("newPasswordCheck", "")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("changePasswordForm", "currentPassword", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("changePasswordForm", "newPassword", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("changePasswordForm", "newPasswordCheck", "NotBlank"));
    }

    @Test
    void 비밀번호_변경_실패_현재_비밀번호_틀림() throws Exception {
        mockMvc.perform(post("/account/change-password")
                        .param("currentPassword", "wrong")
                        .param("newPassword", "password12!@")
                        .param("newPasswordCheck", "password12!@")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasErrors("changePasswordForm"));
    }

    @Test
    void 비밀번호_변경_실패_비밀번호_재확인_틀림() throws Exception {
        mockMvc.perform(post("/account/change-password")
                        .param("currentPassword", PASSWORD)
                        .param("newPassword", "password12!@")
                        .param("newPasswordCheck", "pass")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasErrors("changePasswordForm"));
    }

    @Test
    void 비밀번호_변경_실패_동일한_비밀번호_재사용() throws Exception {
        mockMvc.perform(post("/account/change-password")
                        .param("currentPassword", PASSWORD)
                        .param("newPassword", PASSWORD)
                        .param("newPasswordCheck", PASSWORD)
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasErrors("changePasswordForm"));
    }

    @Test
    void 회원탈퇴_성공() throws Exception {
        mockMvc.perform(post("/account/withdraw")
                        .param("password", PASSWORD)
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void 회원탈퇴_실패_비로그인() throws Exception {
        mockMvc.perform(post("/account/withdraw")
                        .param("password", PASSWORD))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?redirectURL=/account/withdraw"));
    }

    @Test
    void 회원탈퇴_실패_비밀번호_틀림() throws Exception {
        mockMvc.perform(post("/account/withdraw")
                        .param("password", "wrong")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errors"));
    }
}
