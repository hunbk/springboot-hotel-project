package com.ptbh.kyungsunghotel.domain.board;

import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.member.MemberRepository;
import com.ptbh.kyungsunghotel.web.board.PageRequestDto;
import com.ptbh.kyungsunghotel.web.board.PostSaveForm;
import com.ptbh.kyungsunghotel.web.board.PostUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * boardId로 검색한 Board를 BoardDto에 담아서 리턴
     *
     * @param boardId
     * @return BoardDto
     */
    public BoardDto findByBoardId(Long boardId) {
        Board foundBoard = boardRepository.findById(boardId).get();
        return BoardDto.from(foundBoard);
    }

    public Page<BoardDto> findBoards(PageRequestDto pageRequestDto) {
        //정렬 조건
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = pageRequestDto.getPageable(sort);

        //검색 조건
        SearchType searchType = pageRequestDto.getType();
        String query = pageRequestDto.getQuery();

        //검색조건으로 검색
        Page<Board> resultPage = findBoardByQuery(searchType, query, pageable);
        Page<BoardDto> boardPage = resultPage.map(BoardDto::from);

        return boardPage;
    }

    /**
     * searchType 과 query 조건으로 검색한 Board 엔티티를 Page 타입으로 리턴
     *
     * @param searchType
     * @param query
     * @param pageable
     * @return Page of Board Entity
     */
    private Page<Board> findBoardByQuery(SearchType searchType, String query, Pageable pageable) {
        //검색조건이 없음
        if (searchType == null || query.isBlank()) {
            return boardRepository.findAll(pageable);
        }

        Page<Board> resultPage;
        if (searchType == SearchType.T) {
            resultPage = boardRepository.findByTitleContainingIgnoreCase(query, pageable);
        } else if (searchType == SearchType.TC) {
            resultPage = boardRepository.findByTitleOrContent(query, pageable);
        } else if (searchType == SearchType.W) {
            resultPage = boardRepository.findByMember_Nickname(query, pageable);
        } else {
            resultPage = Page.empty(pageable);
        }
        return resultPage;
    }

    @Transactional
    public Long saveBoard(Long memberId, PostSaveForm form) {
        Member member = memberRepository.findById(memberId).get();
        Board board = Board.builder()
                .member(member)
                .title(form.getTitle())
                .content(form.getContent())
                .build();

        Board savedBoard = boardRepository.save(board);
        log.info("savedBoard.id={}", savedBoard.getId());

        return savedBoard.getId();
    }

    @Transactional
    public void updateBoard(Long boardId, PostUpdateForm form) {
        Board foundBoard = boardRepository.findById(boardId).get();
        foundBoard.update(form.getTitle(), form.getContent());
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board foundBoard = boardRepository.findById(boardId).get();
        boardRepository.delete(foundBoard);
    }
}
