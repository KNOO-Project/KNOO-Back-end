package com.woopaca.knoo.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapResponseDto {

    @JsonProperty(value = "post_id")
    private Long postId;

    @JsonProperty(value = "scraps_count")
    private int scrapsCount;

    private Boolean scrapped;

    @Builder
    public PostScrapResponseDto(Long postId, int scrapsCount, Boolean scrapped) {
        this.postId = postId;
        this.scrapsCount = scrapsCount;
        this.scrapped = scrapped;
    }

    public static PostScrapResponseDto ofScrap(final Post post) {
        return PostScrapResponseDto.builder()
                .postId(post.getId())
                .scrapsCount(post.getScrapsCount())
                .scrapped(true)
                .build();
    }

    public static PostScrapResponseDto ofCancelScrap(final Post post) {
        return PostScrapResponseDto.builder()
                .postId(post.getId())
                .scrapsCount(post.getScrapsCount())
                .scrapped(false)
                .build();
    }
}
