package com.ptbh.kyungsunghotel.reserve;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReserveForm {
    private Long id;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private String roomNo;

    private String name;
}
