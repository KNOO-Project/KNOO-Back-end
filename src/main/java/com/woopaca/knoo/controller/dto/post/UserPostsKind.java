package com.woopaca.knoo.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserPostsKind {

    WRITE("write"),
    COMMENT("comment"),
    LIKE("like");

    @JsonValue
    private final String kindName;

    public static UserPostsKind getUserPostsKind(String postsKindName) {
        for (UserPostsKind userPostsKind : UserPostsKind.values()) {
            if (userPostsKind.kindName.equals(postsKindName)) {
                return userPostsKind;
            }
        }

        throw new IllegalArgumentException("유효하지 않은 회원 게시글 종류입니다.");
    }
}
