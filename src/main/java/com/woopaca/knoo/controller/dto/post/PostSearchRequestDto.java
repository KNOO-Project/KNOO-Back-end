package com.woopaca.knoo.controller.dto.post;

import com.woopaca.knoo.entity.attr.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostSearchRequestDto {

    private final PostCategory postCategory;
    private final SearchCondition searchCondition;
    private final String keyword;
    private final int page;
}
