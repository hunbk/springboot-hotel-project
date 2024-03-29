package com.ptbh.kyungsunghotel.web.board;

import com.ptbh.kyungsunghotel.DatabaseCleaner;
import com.ptbh.kyungsunghotel.domain.auth.AuthInfo;
import com.ptbh.kyungsunghotel.domain.board.BoardService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 컨트롤러 테스트지만 여기선 인수 테스트에 더 가까움.
 * 순수 컨트롤러 계층의 테스트는 @WebMvcTest를 사용할 것.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BoardControllerTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private MockMvc mockMvc;

    private Member member;
    private AuthInfo authInfo;

    @BeforeEach
    void setup() {
        member = new Member(LOGIN_ID, PASSWORD, NAME, NICKNAME, EMAIL, CELLPHONE);
        memberRepository.save(member);
        authInfo = new AuthInfo(member.getId(), member.getNickname());
    }

    @AfterEach
    void clear() {
        databaseCleaner.execute();
    }

    @Test
    void 게시판_메인화면() throws Exception {
        mockMvc.perform(get("/boards"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("list"))
                .andExpect(model().attributeExists("pageRequestDto", "boardPage", "searchTypes"))
                .andExpect(view().name("boards/list"));
    }

    @Test
    void 게시판_작성화면() throws Exception {
        mockMvc.perform(get("/boards/save")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("postSaveForm"))
                .andExpect(model().attributeExists("postSaveForm"))
                .andExpect(view().name("boards/postSave"));
    }

    @Test
    void 게시판_작성화면_비로그인() throws Exception {
        mockMvc.perform(get("/boards/save"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?redirectURL=/boards/save"));
    }

    @Test
    void 게시판_작성요청_성공() throws Exception {
        mockMvc.perform(post("/boards/save")
                        .param("title", "제목")
                        .param("content", "내용")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("savePost"))
                .andExpect(view().name("redirect:/boards"))
                .andExpect(redirectedUrl("/boards"));
    }

    @Test
    void 게시판_작성요청_실패_비로그인() throws Exception {
        mockMvc.perform(post("/boards/save")
                        .param("title", "제목")
                        .param("content", "내용"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?redirectURL=/boards/save"));
    }

    @Test
    void 게시판_작성요청_유효성_검증_실패() throws Exception {
        mockMvc.perform(post("/boards/save")
                        .param("title", "")
                        .param("content", "")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("savePost"))
                .andExpect(model().attributeErrorCount("postSaveForm", 2))
                .andExpect(model().attributeHasFieldErrorCode("postSaveForm", "title", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("postSaveForm", "content", "NotBlank"))
                .andExpect(view().name("boards/postSave"));
    }

    @Test
    void 게시판_수정화면() throws Exception {
        //given
        PostSaveForm postSaveForm = new PostSaveForm("제목", "내용");
        Long boardId = boardService.saveBoard(member.getId(), postSaveForm);
        String url = "/boards/" + boardId + "/update";

        //when, then
        mockMvc.perform(get(url)
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("postUpdateForm"))
                .andExpect(model().attributeExists("postUpdateForm"))
                .andExpect(view().name("boards/postUpdate"));
    }

    @Test
    void 게시판_수정화면_비로그인() throws Exception {
        //given
        PostSaveForm postSaveForm = new PostSaveForm("제목", "내용");
        Long boardId = boardService.saveBoard(member.getId(), postSaveForm);
        String url = "/boards/" + boardId + "/update";

        //when, then
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?redirectURL=/boards/" + boardId + "/update"));
    }

    @Test
    void 게시판_수정화면_수정권한없음() throws Exception {
        //given
        AuthInfo another = new AuthInfo(0L, "다른작성자");
        PostSaveForm postSaveForm = new PostSaveForm("제목", "내용");
        Long boardId = boardService.saveBoard(member.getId(), postSaveForm);
        String url = "/boards/" + boardId + "/update";

        //when, then
        mockMvc.perform(get(url)
                        .sessionAttr(SessionConstants.AUTH_INFO, another))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void 게시판_수정요청_성공() throws Exception {
        //given
        PostSaveForm postSaveForm = new PostSaveForm("제목", "내용");
        Long boardId = boardService.saveBoard(member.getId(), postSaveForm);
        String url = "/boards/" + boardId + "/update";

        //when, then
        mockMvc.perform(put(url)
                        .param("title", "수정된 제목")
                        .param("content", "수정된 내용")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("updatePost"))
                .andExpect(redirectedUrl("/boards/" + boardId));
    }

    @Test
    void 게시판_수정요청_실패_비로그인() throws Exception {
        //given
        PostSaveForm postSaveForm = new PostSaveForm("제목", "내용");
        Long boardId = boardService.saveBoard(member.getId(), postSaveForm);
        String url = "/boards/" + boardId + "/update";

        //when, then
        mockMvc.perform(put(url)
                        .param("title", "수정된 제목")
                        .param("content", "수정된 내용"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?redirectURL=/boards/" + boardId + "/update"));
    }

    @Test
    void 게시판_수정요청_실패_수정권한없음() throws Exception {
        //given
        AuthInfo another = new AuthInfo(0L, "다른작성자");
        PostSaveForm postSaveForm = new PostSaveForm("제목", "내용");
        Long boardId = boardService.saveBoard(member.getId(), postSaveForm);
        String url = "/boards/" + boardId + "/update";

        //when, then
        mockMvc.perform(put(url)
                        .param("title", "수정된 제목")
                        .param("content", "수정된 내용")
                        .sessionAttr(SessionConstants.AUTH_INFO, another))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void 게시판_수정요청_유효성_검증_실패() throws Exception {
        //given
        PostSaveForm postSaveForm = new PostSaveForm("제목", "내용");
        Long boardId = boardService.saveBoard(member.getId(), postSaveForm);
        String url = "/boards/" + boardId + "/update";

        //when, then
        mockMvc.perform(put(url)
                        .param("title", "")
                        .param("content", "")
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updatePost"))
                .andExpect(model().attributeErrorCount("postUpdateForm", 2))
                .andExpect(model().attributeHasFieldErrorCode("postUpdateForm", "title", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("postUpdateForm", "content", "NotBlank"))
                .andExpect(view().name("boards/postUpdate"));
    }

    @Test
    void 게시판_삭제요청_성공() throws Exception {
        //given
        PostSaveForm postSaveForm = new PostSaveForm("제목", "내용");
        Long boardId = boardService.saveBoard(member.getId(), postSaveForm);
        String url = "/boards/" + boardId;

        //when, then
        mockMvc.perform(delete(url)
                        .sessionAttr(SessionConstants.AUTH_INFO, authInfo))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void 게시판_삭제요청_실패_비로그인() throws Exception {
        //given
        PostSaveForm postSaveForm = new PostSaveForm("제목", "내용");
        Long boardId = boardService.saveBoard(member.getId(), postSaveForm);
        String url = "/boards/" + boardId;

        //when, then
        mockMvc.perform(delete(url))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 게시판_삭제요청_실패_삭제권한없음() throws Exception {
        //given
        AuthInfo another = new AuthInfo(0L, "다른작성자");
        PostSaveForm postSaveForm = new PostSaveForm("제목", "내용");
        Long boardId = boardService.saveBoard(member.getId(), postSaveForm);
        String url = "/boards/" + boardId;

        //when, then
        mockMvc.perform(delete(url)
                        .sessionAttr(SessionConstants.AUTH_INFO, another))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
