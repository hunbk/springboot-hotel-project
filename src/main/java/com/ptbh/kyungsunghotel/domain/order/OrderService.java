package com.ptbh.kyungsunghotel.domain.order;

import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.member.MemberRepository;
import com.ptbh.kyungsunghotel.domain.reservation.Reservation;
import com.ptbh.kyungsunghotel.domain.reservation.ReservationService;
import com.ptbh.kyungsunghotel.domain.room.RoomService;
import com.ptbh.kyungsunghotel.exception.member.NoSuchMemberException;
import com.ptbh.kyungsunghotel.exception.order.TotalPriceMismatchException;
import com.ptbh.kyungsunghotel.web.order.OrderForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ReservationService reservationService;
    private final RoomService roomService;

    @Transactional
    public void order(Long memberId, OrderForm orderForm) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NoSuchMemberException::new);

        //예약 생성
        Reservation reservation = reservationService.reserve(member, orderForm);

        //가격 검증
        int totalPrice = roomService.getTotalPrice(orderForm.getRoomType(),
                orderForm.getCheckIn(),
                orderForm.getCheckOut(),
                orderForm.getPersonnel());
        if (orderForm.getTotalPrice() != totalPrice) {
            throw new TotalPriceMismatchException();
        }

        //주문 생성
        Order order = Order.createOrder(member, reservation, totalPrice);
        orderRepository.save(order);
    }
}
