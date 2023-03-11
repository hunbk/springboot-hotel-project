package com.ptbh.kyungsunghotel.web.member;

import com.ptbh.kyungsunghotel.domain.auth.AuthInfo;
import com.ptbh.kyungsunghotel.domain.board.BoardDto;
import com.ptbh.kyungsunghotel.domain.board.BoardService;
import com.ptbh.kyungsunghotel.domain.member.MemberDto;
import com.ptbh.kyungsunghotel.domain.member.MemberService;
import com.ptbh.kyungsunghotel.domain.reservation.ReservationService;
import com.ptbh.kyungsunghotel.exception.member.NoSuchMemberException;
import com.ptbh.kyungsunghotel.web.SessionConstants;
import com.ptbh.kyungsunghotel.web.auth.Login;
import com.ptbh.kyungsunghotel.web.reservation.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final ReservationService reservationService;

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("joinForm", new JoinForm());
        return "members/join";
    }

    @PostMapping("/join")
    public String join(@Validated @ModelAttribute JoinForm joinForm,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {

        //비밀번호 재확인 불일치
        if (!joinForm.getPassword().equals(joinForm.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "notSame", "비밀번호가 일치하지 않습니다.");
        }
        //아이디, 닉네임 중복
        if (joinForm.getLoginId() != null && joinForm.getNickname() != null) {
            if (memberService.existsLoginId(joinForm.getLoginId())) {
                bindingResult.rejectValue("loginId", "exists", "이미 사용중인 아이디입니다.");
            }
            if (memberService.existsNickname(joinForm.getNickname())) {
                bindingResult.rejectValue("nickname", "exists", "이미 사용중인 닉네임입니다.");
            }
        }
        if (bindingResult.hasErrors()) {
            return "members/join";
        }

        JoinResponse joinResponse = memberService.join(joinForm);
        redirectAttributes.addFlashAttribute("joinResponse", joinResponse);
        return "redirect:/login";
    }

    //프로필(닉네임, 게시글)
    @GetMapping("/members/{memberId:\\d+}")
    public String profile(@PathVariable Long memberId,
                          @SortDefault(sort = "id", direction = Sort.Direction.DESC) @PageableDefault Pageable pageable,
                          Model model) {

        MemberDto memberDto = memberService.findByMemberId(memberId);
        Page<BoardDto> boardPage = boardService.findBoardsByMemberNickname(memberDto.getNickname(), pageable);

        //프로필 정보는 id와 닉네임만 필요하므로 따로, 혹은 MemberInfo 등의 객체로 넘겨줄 것.
        model.addAttribute("memberId", memberDto.getId());
        model.addAttribute("nickname", memberDto.getNickname());
        model.addAttribute("boardPage", boardPage);
        return "members/profile";
    }

    //내 계정 및 예약조회
    @GetMapping("/account")
    public String account(@SessionAttribute(name = SessionConstants.AUTH_INFO, required = false) AuthInfo authInfo,
                          Model model) {

        //객체 그래프 탐색. N+1 발생
//        List<ReservationDto> reservations = memberService.getReservationsByMemberId(authInfo.getId());

        //jpql fetch join 사용
        List<ReservationDto> reservations = reservationService.findAllByMemberId(authInfo.getId());

        MemberDto memberDto = memberService.findByMemberId(authInfo.getId());
        model.addAttribute("memberDto", memberDto);
        model.addAttribute("reservations", reservations);
        return "members/account";
    }

    @GetMapping("/account/update")
    public String MemberUpdateForm(@Login AuthInfo authInfo,
                                   Model model) {

        MemberDto memberDto = memberService.findByMemberId(authInfo.getId());
        MemberUpdateForm memberUpdateForm = MemberUpdateForm.from(memberDto);
        model.addAttribute("memberUpdateForm", memberUpdateForm);
        return "members/memberUpdate";
    }

    @PostMapping("/account/update")
    public String updateMember(@Validated @ModelAttribute MemberUpdateForm memberUpdateForm,
                               BindingResult bindingResult,
                               @Login AuthInfo authInfo,
                               HttpServletRequest request) {

        //닉네임을 수정한 경우만 중복 검증
        if (!memberUpdateForm.getNickname().equals(authInfo.getNickname())) {
            if (memberService.existsNickname(memberUpdateForm.getNickname())) {
                bindingResult.rejectValue("nickname", "exists", "이미 사용중인 닉네임입니다.");
            }
        }
        if (bindingResult.hasErrors()) {
            return "members/memberUpdate";
        }

        memberService.updateMember(authInfo.getId(), memberUpdateForm);

        //세션의 AuthInfo도 업데이트함.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(SessionConstants.AUTH_INFO, new AuthInfo(authInfo.getId(), memberUpdateForm.getNickname()));
        }

        return "redirect:/account";
    }

    @GetMapping("/account/change-password")
    public String changePasswordForm(Model model) {
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "members/changePassword";
    }

    @PostMapping("/account/change-password")
    public String changePassword(@Validated @ModelAttribute ChangePasswordForm changePasswordForm,
                                 BindingResult bindingResult,
                                 @Login AuthInfo authInfo) {

        if (!bindingResult.hasFieldErrors()) {
            if (!memberService.verifyPassword(authInfo.getId(), changePasswordForm.getCurrentPassword())) {
                bindingResult.reject("currentPasswordNotMatch", "현재 비밀번호가 일치하지 않습니다.");
            }
            if (!changePasswordForm.getNewPassword().equals(changePasswordForm.getNewPasswordCheck())) {
                bindingResult.reject("newPasswordNotMatch", "새 비밀번호가 일치하지 않습니다.");
            }
            if (changePasswordForm.getCurrentPassword().equals(changePasswordForm.getNewPassword())) {
                bindingResult.reject("samePassword", "현재 사용하시는 비밀번호입니다. 새 비밀번호를 입력해주세요.");
            }
        }
        if (bindingResult.hasErrors()) {
            return "members/changePassword";
        }

        memberService.changePassword(authInfo.getId(), changePasswordForm);

        return "redirect:/account";
    }

    //회원 탈퇴
    @GetMapping("/account/withdraw")
    public String withdrawForm() {
        return "members/withdraw";
    }

    @PostMapping("/account/withdraw")
    public String withdraw(@RequestParam String password,
                           @Login AuthInfo authInfo,
                           HttpServletRequest request,
                           Model model) {

        Map<String, String> errors = new HashMap<>();
        if (!StringUtils.hasText(password)) {
            errors.put("required", "비밀번호는 필수입니다.");
        }
        if (errors.isEmpty() && !memberService.verifyPassword(authInfo.getId(), password)) {
            errors.put("globalError", "비밀번호가 일치하지 않습니다.");
        }
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "members/withdraw";
        }

        memberService.deleteByMemberId(authInfo.getId());

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    //예외처리
    //TODO: ControllerAdvice에 적용
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchMemberException.class)
    public String noSuchMemberException() {
        return "error/404";
    }
}
