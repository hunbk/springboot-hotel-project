package com.ptbh.kyungsunghotel.web.room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ReservableRoomSearchRequest {

    @NotNull(message = "체크인 날짜는 필수입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;

    @NotNull(message = "체크아웃 날짜는 필수입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOut;

    @Range(min = 1, max = 6, message = "인원은 1~6인까지 가능합니다.")
    private int personnel;

    public ReservableRoomSearchRequest(LocalDate checkIn, LocalDate checkOut, int personnel) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.personnel = personnel;
    }
}
