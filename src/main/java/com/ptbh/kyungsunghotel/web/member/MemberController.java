package com.ptbh.kyungsunghotel.web.member;

import com.ptbh.kyungsunghotel.domain.board.Board;
import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.member.MemberRepository;
import com.ptbh.kyungsunghotel.domain.member.MemberService;
import com.ptbh.kyungsunghotel.domain.reserve.Reserve;
import com.ptbh.kyungsunghotel.domain.reserve.ReserveRepository;
import com.ptbh.kyungsunghotel.domain.reserve.ReserveService;
import com.ptbh.kyungsunghotel.web.SessionConstants;
import com.ptbh.kyungsunghotel.web.auth.LoginForm;
import com.ptbh.kyungsunghotel.web.reserve.ReserveForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final ReserveRepository reserveRepository;
    private final MemberService memberService;

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

    // 회원 정보 조회
    @GetMapping("/member/info")
    public String showMemberInfo(@SessionAttribute(value = SessionConstants.LOGIN_MEMBER, required = false) Member member, Model model) {
        List<Board> boards = memberRepository.findByLoginId(member.getLoginId()).orElse(null).getBoards();
        boards.sort((o1, o2) -> (int) (o2.getId() - o1.getId()));

        List<Reserve> reserves = memberRepository.findByLoginId(member.getLoginId()).orElse(null).getReserves();
        List<ReserveForm> list = new ArrayList<>();

        reserves = reserves.stream().filter(ReserveService.distinctByKey(Reserve::getReserveId)).collect(Collectors.toList());
        for (Reserve reserve : reserves) {
            ReserveForm reserveForm = new ReserveForm();
            reserveForm.setId(reserve.getId());
            reserveForm.setCheckIn(reserve.getDate());
            reserveForm.setCheckOut(reserve.getDate().plusDays(reserveRepository.countByReserveId(reserve.getReserveId())));
            reserveForm.setRoomNo(reserve.getRoom().getRoomNo());
            reserveForm.setName(reserve.getMember().getName());
            reserveForm.setPersonnel(reserve.getPersonnel());
            reserveForm.setReservePrice(reserve.getReservePrice());
            list.add(reserveForm);
        }

//        model.addAttribute("reserves", reserves);
        model.addAttribute("reserves", list);
        model.addAttribute("boards", boards);
        model.addAttribute("member", member);
        return "members/memberInfo";
    }

    // 회원 정보 수정
    @GetMapping("/member/update")
    public String showMemberUpdateForm(@SessionAttribute(value = SessionConstants.LOGIN_MEMBER, required = false) Member member, Model model) {
        UpdateForm updateForm = new UpdateForm();
        updateForm.setName(member.getName());
        updateForm.setNickname(member.getNickname());
        updateForm.setEmail(member.getEmail());
        updateForm.setCellPhone(member.getCellPhone());
        model.addAttribute("updateForm", updateForm);
        return "members/memberUpdateForm";
    }

    // 매개변수 순서를 지키지 않으면 @Validated 검증을 받을 수 없음!!
    // @Validated, BindingResult 를 순서대로 선언
    @PostMapping("/member/update")
    public String updateMember(@Validated UpdateForm updateForm,
                               BindingResult bindingResult,
                               @SessionAttribute(value = SessionConstants.LOGIN_MEMBER, required = false) Member member) {
        if (bindingResult.hasErrors()) {
            return "members/memberUpdateForm";
        }

        member.update(updateForm.getName(), updateForm.getNickname(), updateForm.getEmail(), updateForm.getCellPhone());

        memberRepository.save(member);
        return "redirect:/member/info";
    }

    @GetMapping("/member/changePassword")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "members/changePasswordForm";
    }

    // 매개변수 순서를 지키지 않으면 @Validated 검증을 받을 수 없음!!
    // @Validated, BindingResult 를 순서대로 선언
    @PostMapping("/member/changePassword")
    public String changePassword(@Validated PasswordForm passwordForm,
                                 BindingResult bindingResult,
                                 @SessionAttribute(value = SessionConstants.LOGIN_MEMBER, required = false) Member member
    ) {
        if (bindingResult.hasErrors()) {
            return "members/changePasswordForm";
        }

        if (!passwordForm.getOldPassword().equals(member.getPassword())) {
            bindingResult.reject("incorrectPassword", "기존 비밀번호가 일치하지 않습니다.");
            return "members/changePasswordForm";
        }

        if (!passwordForm.getNewPassword().equals(passwordForm.getCheckNewPassword())) {
            bindingResult.reject("differentNewPassword", "새 비밀번호가 일치하지 않습니다.");
            return "members/changePasswordForm";
        }

        member.changePassword(passwordForm.getNewPassword());
        memberRepository.save(member);
        return "redirect:/";
    }

    // 회원 탈퇴
    @GetMapping("/member/withdraw")
    public String showWithdraw(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "members/withdraw";
    }

    @PostMapping("/member/withdraw")
    public String withdraw(@Validated LoginForm loginForm,
                           BindingResult bindingResult,
                           @SessionAttribute(value = SessionConstants.LOGIN_MEMBER, required = false) Member member) {
        if (bindingResult.hasErrors()) {
            System.out.println("################ has errors");
            return "members/withdraw";
        }

        if (!member.getLoginId().equals(loginForm.getLoginId())) {
            System.out.println("################ loginIdFail");
            bindingResult.reject("loginIdFail", "아이디가 일치하지 않습니다.");
            return "members/withdraw";
        }

        if (!member.getPassword().equals(loginForm.getPassword())) {
            System.out.println("################ passwordFail");
            bindingResult.reject("passwordFail", "비밀번호가 일치하지 않습니다.");
            return "members/withdraw";
        }

        memberRepository.delete(member);
        return "redirect:/member/logout";
    }
}
