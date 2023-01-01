package com.ptbh.kyungsunghotel.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardRepository extends JpaRepository<Board, Integer> {
    Page<Board> findByTitleContaining(String title, Pageable pageable);

    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    Page<Board> findByMember_LoginId(String loginId, Pageable pageable);
}
