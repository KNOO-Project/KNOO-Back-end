package com.woopaca.knoo.entity.value;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@Slf4j
public enum PostCategory {

    FREE("free"),
    NEWCOMER("newcomer"),
    INFO("info"),
    EMPLOYMENT("employment"),
    GRADUATE("graduate"),
    STUDENT_CLUB("student-club");

    @JsonValue
    private final String categoryName;

    public static PostCategory hasCategoryName(String categoryName) {
        return Arrays.stream(PostCategory.values())
                .filter(category -> category.getCategoryName().equals(categoryName))
                .findFirst()
                .orElse(null);
    }
}
