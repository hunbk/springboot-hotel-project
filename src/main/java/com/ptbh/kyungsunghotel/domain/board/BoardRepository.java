package com.ptbh.kyungsunghotel.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    //제목으로 검색
    Page<Board> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    //제목 또는 내용으로 검색
    @Query("select b from Board b where upper(b.title) like concat('%', upper(:searchText), '%') or upper(b.content) like concat('%', upper(:searchText), '%')")
    Page<Board> findByTitleOrContent(@Param("searchText") String searchText, Pageable pageable);

    //작성자로 검색
    Page<Board> findByMember_Nickname(String nickname, Pageable pageable);

}
