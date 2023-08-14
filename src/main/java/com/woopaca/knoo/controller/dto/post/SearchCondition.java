package com.woopaca.knoo.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SearchCondition {

    ALL("all"),
    TITLE("title"),
    CONTENT("content");

    @JsonValue
    private final String conditionName;

    public static SearchCondition hasSearchCondition(String searchConditionName) {
        return Arrays.stream(SearchCondition.values())
                .filter(searchCondition -> searchCondition.conditionName.equals(searchConditionName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 검색 조건입니다."));
    }
}
