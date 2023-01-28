package com.ptbh.kyungsunghotel.domain.board;

import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.member.MemberRepository;
import com.ptbh.kyungsunghotel.exception.board.NoSuchBoardException;
import com.ptbh.kyungsunghotel.web.board.PostSaveForm;
import com.ptbh.kyungsunghotel.web.board.PostUpdateForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.ptbh.kyungsunghotel.Constants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceUnitTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    private Member member;

    @BeforeEach
    void setup() {
        member = new Member(1L, LOGIN_ID, PASSWORD, NAME, NICKNAME, EMAIL, CELLPHONE);
    }

    @Test
    @DisplayName("게시글 조회 실패 시, 예외가 발생한다.")
    void 게시글_조회_실패_예외발생() {
        //given
        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> boardService.findByBoardId(1L))
                .isInstanceOf(NoSuchBoardException.class);
    }

    @Test
    @DisplayName("게시글 조회 성공 시, 예외가 발생하지 않는다.")
    void 게시글_조회_성공_예외발생안함() {
        //given
        Board board = board();
        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(board));

        //when, then
        assertThatCode(() -> boardService.findByBoardId(1L)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게시글 저장 요청 시, 게시글이 저장된다.")
    void 게시글_저장_성공() {
        //given
        Board board = board();
        PostSaveForm postSaveForm = new PostSaveForm("제목 1", "내용 1");

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(member));
        given(boardRepository.save(any(Board.class)))
                .willReturn(board);

        //when
        Long boardId = boardService.saveBoard(member.getId(), postSaveForm);

        //then
        assertThat(boardId).isEqualTo(1L);
        assertThat(board.getTitle()).isEqualTo(postSaveForm.getTitle());

        //verify
        verify(memberRepository, times(1)).findById(anyLong());
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("게시글 수정 요청 시, 수정이 가능하다.")
    void 게시글_수정_성공() {
        //given
        Board board = board();
        PostUpdateForm postUpdateForm = new PostUpdateForm("수정된 제목", "수정된 내용");

        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(board));
        Long boardId = board.getId();

        //when
        boardService.updateBoard(boardId, postUpdateForm);

        //then
        assertThat(board.getTitle()).isEqualTo(postUpdateForm.getTitle());
        assertThat(board.getContent()).isEqualTo(postUpdateForm.getContent());
    }

    @Test
    @DisplayName("게시글 수정 요청 시, 게시글이 존재하지 않으면 예외가 발생한다.")
    void 게시글_수정_실패_예외발생() {
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