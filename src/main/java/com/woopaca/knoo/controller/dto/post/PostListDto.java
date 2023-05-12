package com.woopaca.knoo.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.entity.Post;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

public class PostListDto {

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

    @JsonProperty(value = "comments_count")
    private int commentsCount;

    @JsonProperty(value = "likes_count")
    private int likesCount;

    @JsonProperty(value = "scraps_count")
    private int scrapsCount;

    @Builder
    public PostListDto(Long postId, String postTitle, String postContent, String postDate,
                       String writerName, int commentsCount, int likesCount, int scrapsCount) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postDate = postDate;
        this.writerName = writerName;
        this.commentsCount = commentsCount;
        this.likesCount = likesCount;
        this.scrapsCount = scrapsCount;
    }

    public static PostListDto from(final Post post) {
        String contentPreview = post.getPostContent();
        if (contentPreview.length() > 20) {
            contentPreview = post.getPostContent().substring(0, 19);
        }

        String formattedDate =
                post.getPostDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        return PostListDto.builder()
                .postId(post.getId())
                .postTitle(post.getPostTitle())
                .postContent(contentPreview)
                .postDate(formattedDate)
                .writerName(post.isAnonymous() ? "KNOOER" : post.getWriter().getName())
                .commentsCount(post.getCommentsCount())
                .likesCount(post.getLikesCount())
                .scrapsCount(post.getScrapsCount())
                .build();
    }
}
