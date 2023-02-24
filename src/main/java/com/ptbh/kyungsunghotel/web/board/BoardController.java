package com.ptbh.kyungsunghotel.web.board;

import com.ptbh.kyungsunghotel.domain.auth.AuthInfo;
import com.ptbh.kyungsunghotel.domain.board.BoardDto;
import com.ptbh.kyungsunghotel.domain.board.BoardService;
import com.ptbh.kyungsunghotel.domain.board.SearchType;
import com.ptbh.kyungsunghotel.exception.auth.NoAuthorityException;
import com.ptbh.kyungsunghotel.exception.board.NoSuchBoardException;
import com.ptbh.kyungsunghotel.web.auth.Login;
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

    @GetMapping("/{boardId:\\d+}")
    public String postView(@PathVariable Long boardId, Model model) {
        BoardDto boardDto = boardService.findByBoardId(boardId);
        model.addAttribute("board", boardDto);
        return "boards/postView";
    }

    @GetMapping("/save")
    public String postSaveForm(Model model) {
        model.addAttribute("postSaveForm", new PostSaveForm());
        return "boards/postSave";
    }

    @PostMapping("/save")
    public String savePost(@Validated @ModelAttribute PostSaveForm postSaveForm,
                           BindingResult bindingResult,
                           @Login AuthInfo authInfo) {

        if (bindingResult.hasErrors()) {
            return "boards/postSave";
        }

        boardService.saveBoard(authInfo.getId(), postSaveForm);

        return "redirect:/boards";
    }

    @GetMapping("/{boardId}/update")
    public String postUpdateForm(@PathVariable Long boardId,
                                 @Login AuthInfo authInfo,
                                 Model model) {

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
                             @Login AuthInfo authInfo) {

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
                                           @Login AuthInfo authInfo) {

        BoardDto boardDto = boardService.findByBoardId(boardId);
        //비로그인
        if (authInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //삭제권한없음
        if (!authInfo.getNickname().equals(boardDto.getWriter())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
    //TODO: ControllerAdvice에 적용
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NoAuthorityException.class)
    public String noAuthorityExceptionHandler() {
        return "error/403";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchBoardException.class)
    public String noSuchBoardException() {
        return "error/404";
    }
}
