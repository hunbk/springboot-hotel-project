package com.ptbh.kyungsunghotel.web.order;

import com.ptbh.kyungsunghotel.domain.auth.AuthInfo;
import com.ptbh.kyungsunghotel.domain.order.OrderService;
import com.ptbh.kyungsunghotel.domain.room.RoomService;
import com.ptbh.kyungsunghotel.exception.reservation.ImpossibleReservationException;
import com.ptbh.kyungsunghotel.web.auth.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final RoomService roomService;

    @GetMapping("/new")
    public String orderForm(@Validated @ModelAttribute OrderPageDto orderPageDto,
                            BindingResult bindingResult,
                            Model model) {

        //올바르지 않은 url 요청(쿼리 파라미터 조작)
        if (bindingResult.hasErrors()) {
            return "error/4xx";
        }

        //예약 가능 여부 검증
        boolean reservable = roomService.reservable(orderPageDto);
        if (!reservable) {
            //TODO: ExceptionHandler 예외 처리
            throw new ImpossibleReservationException();
        }

        //총 가격 계산
        int totalPrice = roomService.getTotalPrice(orderPageDto.getRoomType(),
                orderPageDto.getCheckIn(),
                orderPageDto.getCheckOut(),
                orderPageDto.getPersonnel()
        );

        model.addAttribute("totalPrice", totalPrice);

        return "orders/orderForm";
    }

    @PostMapping("/new")
    public String order(@Validated @ModelAttribute OrderForm orderForm,
                        @Login AuthInfo authInfo) {
        orderService.order(authInfo.getId(), orderForm);
        return "redirect:/account";
    }
}
