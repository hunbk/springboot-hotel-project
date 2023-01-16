package com.ptbh.kyungsunghotel.domain.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long id;
    private Long memberId; //현재 게시글 뷰에서 loginMember랑 작성자를 비교하려고 사용. 리팩토링 대상.
    private String writer;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static BoardDto from(Board board) {
        return new BoardDto(
                board.getId(),
                board.getMember().getId(),
                board.getMember().getNickname(),
                board.getTitle(),
                board.getContent(),
                board.getCreatedDate(),
                board.getModifiedDate()
        );
    }

}
