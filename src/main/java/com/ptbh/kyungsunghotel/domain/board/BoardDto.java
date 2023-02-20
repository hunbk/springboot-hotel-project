package com.ptbh.kyungsunghotel.domain.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardDto {

    private Long id;
    private String writer;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private BoardDto(Long id, String writer, String title, String content, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static BoardDto from(Board board) {
        return new BoardDto(
                board.getId(),
                board.getMember().getNickname(),
                board.getTitle(),
                board.getContent(),
                board.getCreatedDate(),
                board.getModifiedDate()
        );
    }
}
