package com.ptbh.kyungsunghotel.web.reserve;

import com.ptbh.kyungsunghotel.domain.auth.AuthInfo;
import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.member.MemberRepository;
import com.ptbh.kyungsunghotel.domain.reserve.Reserve;
import com.ptbh.kyungsunghotel.domain.reserve.ReserveRepository;
import com.ptbh.kyungsunghotel.exception.member.NoSuchMemberException;
import com.ptbh.kyungsunghotel.web.SessionConstants;
import com.ptbh.kyungsunghotel.domain.room.Room;
import com.ptbh.kyungsunghotel.domain.room.RoomRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReserveController {
    private final RoomRepository roomRepository;
    private final ReserveRepository reserveRepository;
    private final MemberRepository memberRepository;

    public ReserveController(RoomRepository roomRepository, ReserveRepository reserveRepository, MemberRepository memberRepository) {
        this.roomRepository = roomRepository;
        this.reserveRepository = reserveRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/reserve")
    public String showReserve(Model model) {
        return "/reserves/reserve";
    }

    @PostMapping("/reserve")
    public String reserve(@RequestParam("checkIn") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                          @RequestParam("checkOut") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                          @RequestParam("roomNo") String roomNo,
                          @RequestParam("personnel") Integer personnel,
                          @RequestParam("reservePrice") Integer reservePrice,
                          @SessionAttribute(value = SessionConstants.AUTH_INFO, required = false) AuthInfo authInfo) {

        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(NoSuchMemberException::new);
        Long l = Long.parseLong("0");
        for (long day = 0; day < ChronoUnit.DAYS.between(checkIn, checkOut); day++) {
            Reserve reserve = new Reserve();
            reserve.setDate(checkIn.plusDays(day));
            reserve.setMember(member);
            reserve.setRoom(roomRepository.findById(roomNo).orElse(null));
            reserve.setPersonnel(personnel);
            reserve.setReservePrice(reservePrice);
            reserveRepository.save(reserve);

            if (l.equals(Long.parseLong("0"))) {
                l = reserve.getId();
            }
            reserve.setReserveId(l);
            reserveRepository.save(reserve);
        }
        return "redirect:/account";
    }

    @GetMapping("/reserve/searchRoom")
    @ResponseBody
    public List<Room> searchRoom(@RequestParam("checkIn") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                 @RequestParam("checkOut") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut) {

        List<Room> rooms = roomRepository.findAll();
        List<Room> reserveRooms = new ArrayList<>();
        List<Reserve> reserves = reserveRepository.findAllByDateBetween(checkIn, checkOut.minusDays(1));
        for (Reserve reserve : reserves) {
            reserveRooms.add(reserve.getRoom());
        }
        rooms.removeAll(reserveRooms);
        return rooms;
    }

    @GetMapping("/reserve/{id}")
    public String reserveView(@PathVariable("id") Long id, Model model) {

        Reserve reserves = reserveRepository.findById(id).orElse(null);

        ReserveForm reserveForm = new ReserveForm();
        reserveForm.setId(reserves.getId());
        reserveForm.setCheckIn(reserves.getDate());
        reserveForm.setCheckOut(reserves.getDate().plusDays(reserveRepository.countByReserveId(reserves.getReserveId())));
        reserveForm.setRoomNo(reserves.getRoom().getRoomNo());
        reserveForm.setName(reserves.getMember().getName());
        reserveForm.setPersonnel(reserves.getPersonnel());
        reserveForm.setReservePrice(reserves.getReservePrice());

        model.addAttribute("reserve", reserveForm);
        return "/reserves/reserveView";
    }

    @GetMapping("/reserve/reserveDelete/{id}")
    public String reserveDelete(@PathVariable("id") Long id) {

        Reserve reserve = reserveRepository.findById(id).orElse(null);
        reserveRepository.deleteByReserveId(reserve.getReserveId());
        return "redirect:/account";
    }
}
