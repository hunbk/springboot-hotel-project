package com.ptbh.kyungsunghotel.domain.board;

import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;

import static com.ptbh.kyungsunghotel.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    private Member member;

    @BeforeEach
    void init() {
        member = new Member(LOGIN_ID, PASSWORD, NAME, NICKNAME, EMAIL, PHONE_NUMBER);
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
        assertThat(board).isEqualTo(savedBoard);
        System.out.println("savedBoard.getCreatedDate() = " + savedBoard.getCreatedDate());
    }

    @Test
    @DisplayName("게시글을 id로 조회할 수 있다.")
    void findBoard() {
        //given
        Board board = new Board("제목 1", "내용 1", member);

        //when
        Board savedBoard = boardRepository.save(board);
        Board foundBoard = boardRepository.findById(savedBoard.getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

        //then
        assertThat(foundBoard.getId()).isEqualTo(savedBoard.getId());
        assertThat(foundBoard).isEqualTo(savedBoard);
    }

    @Test
    @DisplayName("게시글을 수정할 수 있다.")
    void updateBoard() {
        //given
        Board board = new Board("제목 1", "내용 1", member);

        //when
        Board savedBoard = boardRepository.save(board);
        savedBoard.update("수정된 제목", "수정된 내용");

        //then
        assertThat(savedBoard.getTitle()).isEqualTo("수정된 제목");
        assertThat(savedBoard.getContent()).isEqualTo("수정된 내용");
    }

}