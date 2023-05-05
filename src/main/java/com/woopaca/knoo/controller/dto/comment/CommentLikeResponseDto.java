package com.woopaca.knoo.controller.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.entity.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikeResponseDto {

    @JsonProperty(value = "comment_id")
    private Long commentId;

    @JsonProperty(value = "likes_count")
    private int likesCount;

    private Boolean liked;

    @Builder
    public CommentLikeResponseDto(Long commentId, int likesCount, Boolean liked) {
        this.commentId = commentId;
        this.likesCount = likesCount;
        this.liked = liked;
    }

    public static CommentLikeResponseDto ofLike(final Comment comment) {
        return CommentLikeResponseDto.builder()
                .commentId(comment.getId())
                .likesCount(comment.getLikesCount())
                .liked(true)
                .build();
    }

    public static CommentLikeResponseDto ofUnlike(final Comment comment) {
        return CommentLikeResponseDto.builder()
                .commentId(comment.getId())
                .likesCount(comment.getLikesCount())
                .liked(false)
                .build();
    }
}
