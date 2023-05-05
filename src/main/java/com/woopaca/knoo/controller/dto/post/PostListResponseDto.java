package com.woopaca.knoo.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostListResponseDto {

    @JsonProperty(value = "total_pages")
    private int totalPages;

    private List<PostListDto> posts;

    @Builder
    public PostListResponseDto(int totalPages, List<PostListDto> posts) {
        this.totalPages = totalPages;
        this.posts = posts;
    }

    public static PostListResponseDto from(final Page<Post> postPage) {
        List<PostListDto> postList = new ArrayList<>();
        for (Post post : postPage) {
            postList.add(PostListDto.from(post));
        }
        return PostListResponseDto.builder()
                .totalPages(postPage.getTotalPages())
                .posts(postList)
                .build();
    }

    static class PostListDto {
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

        @Builder
        public PostListDto(Long postId, String postTitle, String postContent,
                           String postDate, String writerName, int commentsCount, int likesCount) {
            this.postId = postId;
            this.postTitle = postTitle;
            this.postContent = postContent;
            this.postDate = postDate;
            this.writerName = writerName;
            this.commentsCount = commentsCount;
            this.likesCount = likesCount;
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
                    .build();
        }
    }
}
