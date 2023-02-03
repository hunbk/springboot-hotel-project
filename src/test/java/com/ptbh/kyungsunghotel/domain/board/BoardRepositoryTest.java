package com.ptbh.kyungsunghotel.domain.board;

import com.ptbh.kyungsunghotel.config.JpaAuditingConfig;
import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.member.MemberRepository;
import com.ptbh.kyungsunghotel.exception.board.NoSuchBoardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.ptbh.kyungsunghotel.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class BoardRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    private Member member;

    @BeforeEach
    void setup() {
        member = new Member(LOGIN_ID, PASSWORD, NAME, NICKNAME, EMAIL, CELLPHONE);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("게시글을 저장할 수 있다.")
    void saveBoard() {
        //given
        Board board = new Board("제목 1", "내용 1", member);

        //when
        Board savedBoard = boardRepository.save(board);

        //then
        assertThat(savedBoard.getId()).isNotNull();
        assertThat(savedBoard.getTitle()).isEqualTo("제목 1");
        assertThat(savedBoard.getContent()).isEqualTo("내용 1");
        assertThat(savedBoard.getCreatedDate()).isNotNull();
    }

    @Test
    @DisplayName("게시글을 수정할 수 있다.")
    void updateBoard() {
        //given
        Board board = new Board("제목 1", "내용 1", member);
        boardRepository.save(board);

        //when
        board.update("수정된 제목", "수정된 내용");
        boardRepository.flush();

        //then
        assertThat(board.getTitle()).isEqualTo("수정된 제목");
        assertThat(board.getContent()).isEqualTo("수정된 내용");
        assertThat(board.getModifiedDate()).isNotEqualTo(board.getCreatedDate());
        assertThat(board.getModifiedDate()).isAfter(board.getCreatedDate());
    }

    @Test
    @DisplayName("게시글을 id로 조회할 수 있다.")
    void findBoard() {
        //given
        Board board = new Board("제목 1", "내용 1", member);

        //when
        boardRepository.save(board);
        Board foundBoard = boardRepository.findById(board.getId())
                .orElseThrow(NoSuchBoardException::new);

        //then
        assertThat(foundBoard.getId()).isEqualTo(board.getId());
        assertThat(foundBoard).isEqualTo(board);
    }

    @Test
    @DisplayName("전체 게시글을 조회할 수 있다.")
    void findAll() {
        //given
        Board board1 = new Board("제목 1", "내용 1", member);
        Board board2 = new Board("제목 2", "내용 2", member);
        boardRepository.save(board1);
        boardRepository.save(board2);

        //when
        List<Board> result = boardRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
    }

}