package com.woopaca.knoo.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostPreviewDto {

    private Long postId;
    private String postTitle;

    public static PostPreviewDto of(Long postId, String postTitle) {
        return new PostPreviewDto(postId, postTitle);
    }
}