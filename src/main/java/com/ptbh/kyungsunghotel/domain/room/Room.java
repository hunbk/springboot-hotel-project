package com.ptbh.kyungsunghotel.domain.room;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity

public class Room {
    @Id
    @Column
    private String roomNo;

    @Column
    private String state;

    @Column
    private Integer price;

    public Room(){
    }

    @Builder
    public Room(String roomNo, String state, Integer price){
        this.roomNo = roomNo;
        this.state = state;
        this.price = price;
    }
}
