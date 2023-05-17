package com.woopaca.knoo.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchCondition {

    ALL("all"),
    TITLE("title"),
    CONTENT("content");

    @JsonValue
    private final String conditionName;

    public static SearchCondition hasSearchCondition(String searchConditionName) {
        for (SearchCondition searchCondition : SearchCondition.values()) {
            if (searchCondition.conditionName.equals(searchConditionName)) {
                return searchCondition;
            }
        }

        return null;
    }
}
