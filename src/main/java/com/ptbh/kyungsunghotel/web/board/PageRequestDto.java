package com.ptbh.kyungsunghotel.web.board;

import com.ptbh.kyungsunghotel.domain.board.SearchType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class PageRequestDto {

    private int page = 0;
    private int size = 10;
    private SearchType type;
    private String query = "";

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page, size, sort);
    }
}
