package com.ptbh.kyungsunghotel.domain.member;

import com.ptbh.kyungsunghotel.domain.reservation.Reservation;
import com.ptbh.kyungsunghotel.exception.member.NoSuchMemberException;
import com.ptbh.kyungsunghotel.web.member.ChangePasswordForm;
import com.ptbh.kyungsunghotel.web.member.JoinForm;
import com.ptbh.kyungsunghotel.web.member.JoinResponse;
import com.ptbh.kyungsunghotel.web.member.MemberUpdateForm;
import com.ptbh.kyungsunghotel.web.reservation.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public JoinResponse join(JoinForm joinForm) {
        Member member = Member.builder()
                .loginId(joinForm.getLoginId())
                .password(joinForm.getPassword())
                .name(joinForm.getName())
                .nickname(joinForm.getNickname())
                .email(joinForm.getEmail())
                .cellPhone(joinForm.getCellPhone())
                .build();
        Member savedMember = memberRepository.save(member);
        return JoinResponse.from(savedMember);
    }

    public MemberDto findByMemberId(Long memberId) {
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(NoSuchMemberException::new);
        return MemberDto.from(foundMember);
    }

    @Transactional
    public void updateMember(Long memberId, MemberUpdateForm memberUpdateForm) {
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(NoSuchMemberException::new);
        foundMember.update(
                memberUpdateForm.getName(),
                memberUpdateForm.getNickname(),
                memberUpdateForm.getEmail(),
                memberUpdateForm.getCellPhone()
        );
    }

    //N+1 발생 로직
//    public List<ReservationDto> getReservationsByMemberId(Long memberId) {
//        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchMemberException::new);
//        return member.getReservations().stream()
//                .map(ReservationDto::from)
//                .collect(Collectors.toList());
//    }

    @Transactional
    public void changePassword(Long memberId, ChangePasswordForm changePasswordForm) {
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(NoSuchMemberException::new);
        foundMember.changePassword(changePasswordForm.getNewPassword());
    }

    @Transactional
    public void deleteByMemberId(Long memberId) {
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(NoSuchMemberException::new);
        memberRepository.delete(foundMember);
    }

    public boolean existsLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    public boolean existsNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public boolean verifyPassword(Long memberId, String password) {
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(NoSuchMemberException::new);
        return foundMember.verifyPassword(password);
    }
}
