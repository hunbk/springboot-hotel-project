package com.ptbh.kyungsunghotel.domain.reserve;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.room.Room;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Reserve {
    @Id
    @Column
    @GeneratedValue
    private Long id;

    @Column
    private long reserveId;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column
    private Integer personnel;

    @Column
    private Integer reservePrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_no")
    @JsonIgnore
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Reserve() {
    }

    @Builder
    public Reserve(Long id,  long reserveId, LocalDate date, Integer personnel, Integer reservePrice, Room room, Member member) {
        this.id = id;
        this.reserveId = reserveId;
        this.date = date;
        this.personnel = personnel;
        this.reservePrice = reservePrice;
        this.room = room;
        this.member = member;
    }
}

