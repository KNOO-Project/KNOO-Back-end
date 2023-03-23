package com.woopaca.knoo.controller.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostListResponseDto {

    @JsonProperty(value = "post_id")
    private Long postId;
    @JsonProperty(value = "post_title")
    private String postTitle;
    @JsonProperty(value = "writer_name")
    private String writerName;

    @Builder
    public PostListResponseDto(Long postId, String postTitle, String writerName) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.writerName = writerName;
    }

    public static PostListResponseDto from(final Post post) {
        return PostListResponseDto.builder()
                .postId(post.getId())
                .postTitle(post.getPostTitle())
                .writerName(post.getWriter().getName())
                .build();
    }
}
