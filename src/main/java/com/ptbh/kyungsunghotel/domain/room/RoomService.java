package com.ptbh.kyungsunghotel.domain.room;

import com.ptbh.kyungsunghotel.exception.reservation.InvalidReservationPeriodException;
import com.ptbh.kyungsunghotel.web.order.OrderPageDto;
import com.ptbh.kyungsunghotel.web.room.ReservableRoomResponse;
import com.ptbh.kyungsunghotel.web.room.ReservableRoomSearchRequest;
import com.ptbh.kyungsunghotel.web.room.ReservableRoomSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    /**
     * 예약 가능한 방 정보(방 타입, 가격) 리스트를 리턴
     */
    public ReservableRoomSearchResponse findReservableRoom(ReservableRoomSearchRequest reservableRoomSearchRequest) {
        LocalDate checkIn = reservableRoomSearchRequest.getCheckIn();
        LocalDate checkOut = reservableRoomSearchRequest.getCheckOut();
        int personnel = reservableRoomSearchRequest.getPersonnel();

        if (checkIn.isEqual(checkOut) || checkIn.isAfter(checkOut)) {
            throw new InvalidReservationPeriodException();
        }

        //예약 가능 방 리스트
        List<Room> reservableRooms = roomRepository.findReservableRooms(checkIn, checkOut, personnel);

        //방 타입, 가격을 가지는 리스트 생성
        List<ReservableRoomResponse> reservableRoomResponses = reservableRooms.stream()
                .map(room -> ReservableRoomResponse.of(
                        room.getRoomType(),
                        room.getRoomType().getName(),
                        checkIn,
                        checkOut,
                        personnel,
                        room.getPhotoUrl(),
                        room.calculatePricePerOneDay(personnel))
                )
                .distinct()
                .collect(Collectors.toList());

        return ReservableRoomSearchResponse.of(reservableRoomResponses);
    }


    //예약 가능 확인
    public boolean reservable(OrderPageDto orderPageDto) {
        List<Room> rooms = roomRepository.findReservableRoomsByRoomType(orderPageDto.getRoomType(),
                orderPageDto.getCheckIn(),
                orderPageDto.getCheckOut());
        if (rooms.size() == 0) {
            return false;
        }
        if (rooms.get(0).getMaxPersonnel() < orderPageDto.getPersonnel()) {
            return false;
        }
        return true;
    }

    //총 가격
    //TODO: 재사용되는 메서드. 객체지향적으로 설계하기
    public int getTotalPrice(RoomType roomType, LocalDate checkIn, LocalDate checkOut, int personnel) {
        int useDay = Period.between(checkIn, checkOut).getDays();
        Room room = roomRepository.getFirstByRoomType(roomType);
        return room.calculateTotalPrice(personnel, useDay);
    }
}
