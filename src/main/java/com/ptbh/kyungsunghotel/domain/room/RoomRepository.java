package com.ptbh.kyungsunghotel.domain.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    //체크인, 아웃 날짜에 모든 빈 방 조회
    @Query("SELECT r FROM Room r " +
            "WHERE r.maxPersonnel >= :personnel " +
            "AND r.id NOT IN " +
            "(SELECT DISTINCT rsv.room.id FROM Reservation rsv WHERE rsv.checkIn < :checkOut AND rsv.checkOut > :checkIn)")
    List<Room> findReservableRooms(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut, @Param("personnel") Integer personnel);

    //체크인, 아웃 날짜에 방 타입으로 빈 방 조회
    @Query("SELECT r FROM Room r " +
            "WHERE r.roomType = :roomType " +
            "AND r.id NOT IN " +
            "(SELECT DISTINCT rsv.room.id FROM Reservation rsv WHERE rsv.checkIn < :checkOut AND rsv.checkOut > :checkIn)")
    List<Room> findReservableRoomsByRoomType(@Param("roomType") RoomType roomType, @Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);

    Room getFirstByRoomType(RoomType roomType);

}
