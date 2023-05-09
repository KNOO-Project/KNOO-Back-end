package com.woopaca.knoo.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeResponseDto {

    @JsonProperty(value = "post_id")
    private Long postId;

    @JsonProperty(value = "likes_count")
    private int likesCount;

    private Boolean liked;

    @Builder
    public PostLikeResponseDto(Long postId, int likesCount, Boolean liked) {
        this.postId = postId;
        this.likesCount = likesCount;
        this.liked = liked;
    }

    public static PostLikeResponseDto ofLike(final Post post) {
        return PostLikeResponseDto.builder()
                .postId(post.getId())
                .likesCount(post.getLikesCount())
                .liked(true)
                .build();
    }

    public static PostLikeResponseDto ofCancelLike(final Post post) {
        return PostLikeResponseDto.builder()
                .postId(post.getId())
                .likesCount(post.getLikesCount())
                .liked(false)
                .build();
    }
}
