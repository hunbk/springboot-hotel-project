package com.ptbh.kyungsunghotel.web.member;

import com.ptbh.kyungsunghotel.domain.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping(value = "/validations", params = "loginId")
    public ResponseEntity<Boolean> validateLoginId(@RequestParam String loginId) {
        return ResponseEntity.ok(memberService.existsLoginId(loginId));
    }

    @GetMapping(value = "/validations", params = "nickname")
    public ResponseEntity<Boolean> validateNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(memberService.existsNickname(nickname));
    }
}
