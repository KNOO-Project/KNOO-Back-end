package com.woopaca.knoo.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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

    @JsonCreator
    public static PostCategory hasCategoryName(String categoryName) {
        log.info("hasCategoryName 호출");
        for (PostCategory postCategory : PostCategory.values()) {
            if (postCategory.categoryName.equals(categoryName)) {
                return postCategory;
            }
        }

        return null;
    }
}
