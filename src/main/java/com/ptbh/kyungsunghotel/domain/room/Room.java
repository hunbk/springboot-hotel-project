package com.ptbh.kyungsunghotel.domain.room;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer id;

    private Integer roomNo;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private int defaultPersonnel;

    private int maxPersonnel;

    //1박기준 가격
    private int price;

    private String photoUrl;

    //TODO: 테스트용 데이터 생성시에만 사용됨. 추후 제거
    @Builder
    public Room(Integer roomNo, RoomType roomType, int defaultPersonnel, int maxPersonnel, int price, String photoUrl) {
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.defaultPersonnel = defaultPersonnel;
        this.maxPersonnel = maxPersonnel;
        this.price = price;
        this.photoUrl = photoUrl;
    }

    /**
     * 1박 가격 계산
     * 1인추가 20000원
     * 1박 기준 가격 = 방 가격 + 인원추가 가격
     */
    public int calculatePricePerOneDay(int personnel) {
        //TODO: 추가금액(20000원) 하드코딩 제거
        int additionalPrice = (personnel > defaultPersonnel)
                ? (personnel - defaultPersonnel) * 20000
                : 0;
        int pricePerOneDay = price + additionalPrice;
        return pricePerOneDay;
    }

    /**
     * 총 가격 계산
     * (방 가격 + 인원추가 가격) * 사용 일
     */
    public int calculateTotalPrice(int personnel, int useDay) {
        int additionalPrice = (personnel > defaultPersonnel)
                ? (personnel - defaultPersonnel) * 20000
                : 0;
        int totalPrice = (getPrice() + additionalPrice) * useDay;
        return totalPrice;
    }
}
