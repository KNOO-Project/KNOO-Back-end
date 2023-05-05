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

    private int likes;

    private Boolean liked;

    @Builder
    public PostLikeResponseDto(Long postId, int likes, Boolean liked) {
        this.postId = postId;
        this.likes = likes;
        this.liked = liked;
    }

    public static PostLikeResponseDto ofLike(final Post post) {
        return PostLikeResponseDto.builder()
                .postId(post.getId())
                .likes(post.getLikesCount())
                .liked(true)
                .build();
    }

    public static PostLikeResponseDto ofUnlike(final Post post) {
        return PostLikeResponseDto.builder()
                .postId(post.getId())
                .likes(post.getLikesCount())
                .liked(false)
                .build();
    }
}
