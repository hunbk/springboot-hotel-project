package com.ptbh.kyungsunghotel.domain.reservation;

import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.room.Room;
import com.ptbh.kyungsunghotel.domain.room.RoomRepository;
import com.ptbh.kyungsunghotel.exception.reservation.ImpossibleReservationException;
import com.ptbh.kyungsunghotel.web.order.OrderForm;
import com.ptbh.kyungsunghotel.web.reservation.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    /**
     * 예약 생성
     */
    public Reservation reserve(Member member, OrderForm orderForm) {
        List<Room> reservableRooms = roomRepository.findReservableRoomsByRoomType(orderForm.getRoomType(),
                orderForm.getCheckIn(),
                orderForm.getCheckOut());
        Room room = reservableRooms.stream().findFirst().orElse(null);
        if (room == null) {
            throw new ImpossibleReservationException();
        }

        return Reservation.createReservation(orderForm.getCheckIn(),
                orderForm.getCheckOut(),
                orderForm.getPersonnel(),
                room,
                member);
    }

    public List<ReservationDto> findAllByMemberId(Long memberId) {
        return reservationRepository.findAllByMemberId(memberId).stream()
                .map(ReservationDto::from)
                .collect(Collectors.toList());
    }
}
