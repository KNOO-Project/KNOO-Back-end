package com.woopaca.knoo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostPreviewDto {

    @JsonProperty(value = "post_id")
    private Long postId;
    @JsonProperty(value = "post_title")
    private String postTitle;

    public static PostPreviewDto of(Long postId, String postTitle) {
        return new PostPreviewDto(postId, postTitle);
    }
}