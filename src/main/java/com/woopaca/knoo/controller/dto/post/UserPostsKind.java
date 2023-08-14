package com.woopaca.knoo.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserPostsKind {

    WRITE("write"),
    COMMENT("comment"),
    LIKE("like");

    @JsonValue
    private final String kindName;

    public static UserPostsKind getUserPostsKind(String postsKindName) {
        return Arrays.stream(UserPostsKind.values())
                .filter(userPostsKind -> userPostsKind.kindName.equals(postsKindName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 회원 게시글 종류입니다."));
    }
}
