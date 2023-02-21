package com.ptbh.kyungsunghotel.web.auth;

import com.ptbh.kyungsunghotel.DatabaseCleaner;
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

/**
 * 컨트롤러 테스트지만 여기선 인수 테스트에 더 가까움.
 * 순수 컨트롤러 계층의 테스트는 @WebMvcTest를 사용할 것.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;

    private Member member;

    @BeforeEach
    void setup() {
        member = new Member(LOGIN_ID, PASSWORD, NAME, NICKNAME, EMAIL, CELLPHONE);
        memberRepository.save(member);
    }

    @AfterEach
    void clear() {
        databaseCleaner.execute();
    }

    @Test
    void 로그인_화면() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("loginForm"))
                .andExpect(model().attributeExists("loginForm"))
                .andExpect(view().name("login"));
    }

    @Test
    void 로그인_요청_성공() throws Exception {
        mockMvc.perform(post("/login")
                        .param("loginId", LOGIN_ID)
                        .param("password", PASSWORD))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("login"))
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void 로그인_요청_아이디_비밀번호_틀림() throws Exception {
        mockMvc.perform(post("/login")
                        .param("loginId", "qwer")
                        .param("password", "qwer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(request().sessionAttributeDoesNotExist(SessionConstants.AUTH_INFO))
                .andExpect(model().attributeExists("loginForm"))
                .andExpect(model().attributeHasErrors("loginForm"));
    }

    @Test
    void 로그인_요청_유효성_검증_실패() throws Exception {
        mockMvc.perform(post("/login")
                        .param("loginId", "qwer")
                        .param("password", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(request().sessionAttributeDoesNotExist(SessionConstants.AUTH_INFO))
                .andExpect(model().attributeExists("loginForm"))
                .andExpect(model().attributeHasFieldErrorCode("loginForm", "password", "NotBlank"));
    }
}
