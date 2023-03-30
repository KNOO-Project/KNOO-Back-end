package com.woopaca.knoo.controller.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetailsResponseDto {

    @JsonProperty(value = "post")
    private PostDetailsDto postDetailsDto;
    private List<CommentListDto> comments;

    @Builder
    public PostDetailsResponseDto(PostDetailsDto postDetailsDto,
                                  List<CommentListDto> comments) {
        this.postDetailsDto = postDetailsDto;
        this.comments = comments;
    }

    public static PostDetailsResponseDto of(final Post post, final List<Comment> comments, final User authenticatedUser) {
        PostDetailsDto postDetails = PostDetailsDto.of(post, authenticatedUser);
        List<CommentListDto> commentList = new ArrayList<>();
        for (Comment comment : comments) {
            commentList.add(CommentListDto.of(comment, authenticatedUser));
        }

        return PostDetailsResponseDto.builder()
                .postDetailsDto(postDetails)
                .comments(commentList)
                .build();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class PostDetailsDto {

        @JsonProperty(value = "post_title")
        private String postTitle;
        @JsonProperty(value = "post_content")
        private String postContent;
        @JsonProperty(value = "post_date")
        private String postDate;
        @JsonProperty(value = "writer_name")
        private String writerName;
        @JsonProperty(value = "is_written_by_user")
        private Boolean isWrittenByUser;

        @Builder
        public PostDetailsDto(String postTitle, String postContent, String postDate,
                              String writerName, boolean isWrittenByUser) {
            this.postTitle = postTitle;
            this.postContent = postContent;
            this.postDate = postDate;
            this.writerName = writerName;
            this.isWrittenByUser = isWrittenByUser;
        }

        public static PostDetailsDto of(final Post post, final User authenticatedUser) {
            User writer = post.getWriter();
            String writerName = post.isAnonymous() ? "KNOOER" : writer.getName();
            boolean isWrittenByUser = writer == authenticatedUser;

            return PostDetailsDto.builder()
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .postDate(post.getPostDate())
                    .writerName(writerName)
                    .isWrittenByUser(isWrittenByUser)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class CommentListDto {

        @JsonProperty(value = "comment_content")
        private String commentContent;
        @JsonProperty(value = "comment_date")
        private String commentDate;
        @JsonProperty(value = "writer_name")
        private String writerName;
        @JsonProperty(value = "is_written_by_user")
        private Boolean isWrittenByUser;

        @Builder
        public CommentListDto(String commentContent, String commentDate,
                              String writerName, boolean isWrittenByUser) {
            this.commentContent = commentContent;
            this.commentDate = commentDate;
            this.writerName = writerName;
            this.isWrittenByUser = isWrittenByUser;
        }

        public static CommentListDto of(final Comment comment, final User authenticatedUser) {
            User writer = comment.getWriter();
            boolean isWrittenByUser = authenticatedUser == writer;
            String writerName = writer.getName();
            if (isWrittenByUser) {
                writerName = "글쓴이";
            }

            return CommentListDto.builder()
                    .commentContent(comment.getCommentContent())
                    .commentDate(comment.getCommentDate())
                    .writerName(writerName)
                    .isWrittenByUser(isWrittenByUser)
                    .build();
        }
    }
}
