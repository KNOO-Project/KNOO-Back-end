package com.woopaca.knoo.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.woopaca.knoo.exception.post.PostError;
import com.woopaca.knoo.exception.post.impl.PostCategoryNotFound;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
        for (PostCategory postCategory : PostCategory.values()) {
            if (postCategory.categoryName.equals(categoryName)) {
                return postCategory;
            }
        }
        
        throw new PostCategoryNotFound(PostError.POST_CATEGORY_NOT_FOUND);
    }
}
