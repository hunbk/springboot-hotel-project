package com.ptbh.kyungsunghotel.web.reservation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationController {

    @GetMapping("/reservation")
    public String reservationForm() {
        return "reservations/reservation";
    }
}
