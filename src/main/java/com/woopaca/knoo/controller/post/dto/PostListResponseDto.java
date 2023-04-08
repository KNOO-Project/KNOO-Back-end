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

    @JsonProperty(value = "post_content")
    private String postContent;

    @JsonProperty(value = "post_date")
    private String postDate;

    @JsonProperty(value = "writer_name")
    private String writerName;

    @Builder
    public PostListResponseDto(Long postId, String postTitle, String postContent,
                               String postDate, String writerName) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postDate = postDate;
        this.writerName = writerName;
    }

    public static PostListResponseDto from(final Post post) {
        String contentPreview = post.getPostContent();
        if (contentPreview.length() > 20) {
            contentPreview = post.getPostContent().substring(0, 19);
        }

        return PostListResponseDto.builder()
                .postId(post.getId())
                .postTitle(post.getPostTitle())
                .postContent(contentPreview)
                .postDate(post.getPostDate())
                .writerName(post.isAnonymous() ? "KNOOER" : post.getWriter().getName())
                .build();
    }
}
