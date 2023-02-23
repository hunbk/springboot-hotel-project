package com.ptbh.kyungsunghotel.web.auth;

import com.ptbh.kyungsunghotel.domain.auth.AuthInfo;
import com.ptbh.kyungsunghotel.domain.auth.AuthService;
import com.ptbh.kyungsunghotel.web.SessionConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        AuthInfo authInfo = authService.login(loginForm);
        if (authInfo == null) {
            bindingResult.reject("idOrPasswordMismatch", "아이디 또는 비밀번호가 일치하지 않습니다. 입력하신 내용을 다시 확인해주세요.");
            if (loginForm.getLoginId().contains(" ")) {
                loginForm.setLoginId("");
            }
            return "login";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.AUTH_INFO, authInfo);

        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
