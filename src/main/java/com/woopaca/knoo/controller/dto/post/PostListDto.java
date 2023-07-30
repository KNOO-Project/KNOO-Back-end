package com.woopaca.knoo.controller.dto.post;

import com.woopaca.knoo.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostListDto {

    private Long postId;

    private String postTitle;

    private String postContent;

    private String postCategory;

    private String postDate;

    private String writerName;

    private int commentsCount;

    private int likesCount;

    private int scrapsCount;

    @Builder
    public PostListDto(Long postId, String postTitle, String postContent, String postCategory, String postDate,
                       String writerName, int commentsCount, int likesCount, int scrapsCount) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postCategory = postCategory;
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
                .postCategory(post.getPostCategory().getCategoryName())
                .postDate(formattedDate)
                .writerName(post.isAnonymous() ? "KNOOER" : post.getWriter().getName())
                .commentsCount(post.getCommentsCount())
                .likesCount(post.getLikesCount())
                .scrapsCount(post.getScrapsCount())
                .build();
    }
}
