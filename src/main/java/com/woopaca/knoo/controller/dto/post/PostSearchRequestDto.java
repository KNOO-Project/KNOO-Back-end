package com.woopaca.knoo.controller.dto.post;

import com.woopaca.knoo.entity.value.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class PostSearchRequestDto {

    private final PostCategory category;
    private final SearchCondition condition;
    @Size(min = 2, message = "검색어는 2자 이상이어야 합니다.")
    private final String keyword;
    @Positive(message = "유효하지 않은 페이지입니다.")
    private final int page;
}
