package com.ptbh.kyungsunghotel.web.board;

import com.ptbh.kyungsunghotel.domain.auth.AuthInfo;
import com.ptbh.kyungsunghotel.domain.board.BoardDto;
import com.ptbh.kyungsunghotel.domain.board.BoardService;
import com.ptbh.kyungsunghotel.domain.board.SearchType;
import com.ptbh.kyungsunghotel.exception.auth.NoAuthorityException;
import com.ptbh.kyungsunghotel.web.SessionConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String list(@ModelAttribute("pageRequestDto") PageRequestDto pageRequestDto, Model model) {
        Page<BoardDto> boardPage = boardService.findBoards(pageRequestDto);
        model.addAttribute("boardPage", boardPage);
        model.addAttribute("searchTypes", SearchType.values());

        return "boards/list";
    }

    @GetMapping("/{boardId}")
    public String postView(@PathVariable Long boardId, Model model) {
        BoardDto boardDto = boardService.findByBoardId(boardId);
        model.addAttribute("board", boardDto);
        return "boards/postView";
    }

    @GetMapping("/save")
    public String postSaveForm(@SessionAttribute(value = SessionConstants.AUTH_INFO, required = false) AuthInfo authInfo,
                               Model model) {

        if (authInfo == null) {
            return "redirect:/login";
        }

        model.addAttribute("postSaveForm", new PostSaveForm());
        return "boards/postSave";
    }

    @PostMapping("/save")
    public String savePost(@Validated @ModelAttribute PostSaveForm postSaveForm,
                           BindingResult bindingResult,
                           //TODO @LoginMember 어노테이션으로 개선
                           @SessionAttribute(value = SessionConstants.AUTH_INFO, required = false) AuthInfo authInfo) {

        if (authInfo == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            return "boards/postSave";
        }

        boardService.saveBoard(authInfo.getId(), postSaveForm);

        return "redirect:/boards";
    }

    @GetMapping("/{boardId}/update")
    public String postUpdateForm(@PathVariable Long boardId,
                                 @SessionAttribute(value = SessionConstants.AUTH_INFO, required = false) AuthInfo authInfo,
                                 Model model) {

        if (authInfo == null) {
            return "redirect:/login";
        }

        BoardDto boardDto = boardService.findByBoardId(boardId);
        authenticate(authInfo, boardDto.getWriter());

        PostUpdateForm postUpdateForm = PostUpdateForm.from(boardDto);
        model.addAttribute("postUpdateForm", postUpdateForm);

        return "boards/postUpdate";
    }

    @PutMapping("/{boardId}/update")
    public String updatePost(@PathVariable Long boardId,
                             @Validated @ModelAttribute PostUpdateForm postUpdateForm,
                             BindingResult bindingResult,
                             @SessionAttribute(value = SessionConstants.AUTH_INFO, required = false) AuthInfo authInfo) {

        if (authInfo == null) {
            return "redirect:/login";
        }

        BoardDto boardDto = boardService.findByBoardId(boardId);
        authenticate(authInfo, boardDto.getWriter());

        if (bindingResult.hasErrors()) {
            return "boards/postUpdate";
        }

        boardService.updateBoard(boardId, postUpdateForm);

        return "redirect:/boards/{boardId}";
    }

    @DeleteMapping("/{boardId}")
    @ResponseBody
    public ResponseEntity<Void> deletePost(@PathVariable Long boardId,
                                           @SessionAttribute(value = SessionConstants.AUTH_INFO, required = false) AuthInfo authInfo) {

        BoardDto boardDto = boardService.findByBoardId(boardId);
        if (authInfo == null || !authInfo.getNickname().equals(boardDto.getWriter())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }

    private static void authenticate(AuthInfo authInfo, String writer) {
        if (!authInfo.getNickname().equals(writer)) {
            throw new NoAuthorityException();
        }
    }

    //예외처리
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NoAuthorityException.class)
    public String NoAuthorityExceptionHandler() {
        return "error/403";
    }
}
