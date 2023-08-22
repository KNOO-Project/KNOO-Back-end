package com.woopaca.knoo.controller.dto.post;

import com.woopaca.knoo.common.DateFormatter;
import com.woopaca.knoo.entity.Image;
import com.woopaca.knoo.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String thumbnail;
    private int imagesCount;

    @Builder
    public PostListDto(Long postId, String postTitle, String postContent, String postCategory, String postDate,
                       String writerName, int commentsCount, int likesCount, int scrapsCount, String thumbnail, int imagesCount) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postCategory = postCategory;
        this.postDate = postDate;
        this.writerName = writerName;
        this.commentsCount = commentsCount;
        this.likesCount = likesCount;
        this.scrapsCount = scrapsCount;
        this.thumbnail = thumbnail;
        this.imagesCount = imagesCount;
    }

    public static PostListDto from(final Post post) {
        String contentPreview = post.getPostContent();
        if (contentPreview.length() > 20) {
            contentPreview = post.getPostContent().substring(0, 19);
        }

        String formattedDate =
                post.getPostDate().format(DateFormatter.getFormatter());
        Image thumbnail = post.getThumbnail();
        String thumbnailUrl = null;
        if (thumbnail != null) {
            thumbnailUrl = thumbnail.getImageUrl();
        }

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
                .thumbnail(thumbnailUrl)
                .imagesCount(post.getImagesCount())
                .build();
    }
}
