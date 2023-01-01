package com.ptbh.kyungsunghotel.web.board;

import com.ptbh.kyungsunghotel.domain.board.Board;
import com.ptbh.kyungsunghotel.domain.board.BoardRepository;
import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.web.SessionConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@Controller
public class BoardController {
    private final BoardRepository boardRepository;

    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @GetMapping("/board/list")
    public String showList(Model model, @PageableDefault(page = 0, size = 20, sort = "boardNo", direction = Sort.Direction.DESC) Pageable pageable,
                           @RequestParam(required = false, defaultValue = "") String searchField,
                           @RequestParam(required = false, defaultValue = "") String searchText) {

        Page<Board> boards = boardRepository.findAll(pageable);

        if (searchField.equals("title")) {
            boards = boardRepository.findByTitleContaining(searchText, pageable);
        } else if (searchField.equals("title2")) {
            boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
        } else if (searchField.equals("writer")) {
            boards = boardRepository.findByMember_LoginId(searchText, pageable);
        }

        int cnt = (int) boards.getTotalElements();
        int totalPage = Math.max(1, boards.getTotalPages());
        int nowPage = boards.getPageable().getPageNumber() + 1; // == pageable.getPageNumber 현재페이지 가져오기
        int startPage = Math.max(1, nowPage - 5); //((nowPage)/pageBlock) * pageBlock + 1;
        int endPage = Math.min(totalPage, nowPage + 4); //startPage + pageBlock - 1;
        if (endPage > totalPage) endPage = totalPage;  // endPage= totalPage<endPage? totalPage:endPage;

        model.addAttribute("boards", boards);
        model.addAttribute("cnt", cnt);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("searchField", searchField);

        return "boards/list";
    }

    @GetMapping("/board/post")
    public String showPost(Model model) {
        model.addAttribute("postForm", new PostForm());
        return "/boards/post";
    }

    @PostMapping("/board/post")
    public String post(@Validated PostForm postForm, BindingResult bindingResult,
                       @SessionAttribute(value = SessionConstants.LOGIN_MEMBER, required = false) Member member) {
        if (bindingResult.hasErrors()) {
            return "/boards/post";
        }
        Board board = new Board();
        board.setMember(member);
        board.setTitle(postForm.getTitle());
        board.setContent(postForm.getContent());
        board.setCreateTime(LocalDateTime.now());
        boardRepository.save(board);
        return "redirect:/board/list";
    }

    @GetMapping("/board/{boardNo}")
    public String postView(@PathVariable("boardNo") Integer boardNo, Model model) {
        PostViewForm postViewForm = new PostViewForm();
        Board board = boardRepository.findById(boardNo).orElse(null);
        postViewForm.setBoardNo(board.getBoardNo());
        postViewForm.setWriter(board.getMember().getName());
        postViewForm.setLoginId(board.getMember().getLoginId());
        postViewForm.setContent(board.getContent());
        postViewForm.setTitle(board.getTitle());
        postViewForm.setCreateTime(board.getCreateTime());
        model.addAttribute("postView", postViewForm);
        return "/boards/postView";
    }

    @GetMapping("/board/postUpdate/{boardNo}")
    public String showPostUpdate(@PathVariable("boardNo") Integer boardNo, Model model) {
        PostUpdateForm postUpdateForm = new PostUpdateForm();
        Board board = boardRepository.findById(boardNo).orElse(null);
        postUpdateForm.setBoardNo(board.getBoardNo());
        postUpdateForm.setTitle(board.getTitle());
        postUpdateForm.setContent(board.getContent());
        model.addAttribute("postUpdate", postUpdateForm);
        return "boards/postUpdate";
    }

    @PostMapping("/board/postUpdate/{boardNo}")
    public String postUpdate(@PathVariable("boardNo") Integer boardNo,
                             @Validated PostUpdateForm postUpdateForm,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "boards/postUpdate";
        }
        Board board = boardRepository.findById(boardNo).orElse(null);
        board.setTitle(postUpdateForm.getTitle());
        board.setContent(postUpdateForm.getContent());
        boardRepository.save(board);
        return "redirect:/board/" + boardNo;
    }

    @GetMapping("/board/postDelete/{boardNo}")
    public String postDelete(@PathVariable("boardNo") Integer boardNo) {
        Board board = boardRepository.findById(boardNo).orElse(null);
        boardRepository.delete(board);
        return "redirect:/board/list";
    }
}


