package com.ptbh.kyungsunghotel.web.order;

import com.ptbh.kyungsunghotel.domain.room.RoomType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class OrderPageDto {

    @NotNull
    private RoomType roomType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOut;

    @Range(min = 1, max = 6, message = "인원은 1~6인까지 가능합니다.")
    private int personnel;

}
