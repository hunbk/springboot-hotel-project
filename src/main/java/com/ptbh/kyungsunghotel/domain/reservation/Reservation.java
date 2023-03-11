package com.ptbh.kyungsunghotel.domain.reservation;

import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.order.Order;
import com.ptbh.kyungsunghotel.domain.room.Room;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false, length = 1)
    private int personnel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //양방향 매핑
//    @OneToOne(mappedBy = "reservation", fetch = FetchType.LAZY)
//    private Order order;

    private Reservation(LocalDate checkIn, LocalDate checkOut, int personnel, Room room, Member member) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.personnel = personnel;
        this.room = room;
        this.member = member;
    }

    //TODO: 테스트용 데이터 생성시에만 사용됨. 추후 제거
    @Builder
    public Reservation(Long id, LocalDateTime createdDate, LocalDate checkIn, LocalDate checkOut, int personnel, Room room, Member member) {
        this.id = id;
        this.createdDate = createdDate;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.personnel = personnel;
        this.room = room;
        this.member = member;
    }

    public static Reservation createReservation(LocalDate checkIn, LocalDate checkOut, int personnel, Room room, Member member) {
        return new Reservation(checkIn, checkOut, personnel, room, member);
    }
}
