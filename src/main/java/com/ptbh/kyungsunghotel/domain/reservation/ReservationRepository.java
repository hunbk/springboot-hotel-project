package com.ptbh.kyungsunghotel.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT rsv FROM Reservation rsv " +
            "JOIN FETCH rsv.room " +
            "WHERE rsv.member.id = :memberId")
    List<Reservation> findAllByMemberId(@Param("memberId") Long memberId);

}
