package com.ptbh.kyungsunghotel.domain.board;

import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.member.MemberRepository;
import com.ptbh.kyungsunghotel.exception.board.NoSuchBoardException;
import com.ptbh.kyungsunghotel.web.board.PostUpdateForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.ptbh.kyungsunghotel.Constants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    private Member member;

    @BeforeEach
    void setup() {
        member = new Member(LOGIN_ID, PASSWORD, NAME, NICKNAME, EMAIL, CELLPHONE);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("게시글 조회 실패 시, 예외가 발생한다.")
    void noSuchBoardException() {
        assertThatThrownBy(() -> boardService.findByBoardId(1L))
                .isInstanceOf(NoSuchBoardException.class);
    }

    @Test
    @DisplayName("게시글 수정 요청 시, 수정이 가능하다.")
    void updateBoard() {
        //given
        Board board = board();
        PostUpdateForm postUpdateForm = new PostUpdateForm("수정된 제목", "수정된 내용");

        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(board));
        Long boardId = board.getId();

        //when
        boardService.updateBoard(boardId, postUpdateForm);

        //then
        assertThat(board.getTitle()).isEqualTo("수정된 제목");
        assertThat(board.getContent()).isEqualTo("수정된 내용");

    }

    @Test
    @DisplayName("게시글 수정 요청 시, 게시글이 존재하지 않으면 예외가 발생한다.")
    void updateBoardNoSuchException() {
        //given
        Board board = board();
        PostUpdateForm postUpdateForm = new PostUpdateForm("수정된 제목", "수정된 내용");

        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        Long boardId = board.getId();

        //when, then
        assertThatThrownBy(() -> boardService.updateBoard(boardId, postUpdateForm))
                .isInstanceOf(NoSuchBoardException.class);
    }

    private Board board() {
        return Board.builder()
                .id(1L)
                .title("제목 1")
                .content("내용 1")
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .member(member)
                .build();
    }
}