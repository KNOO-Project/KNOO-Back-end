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
            commentList.add(CommentListDto.of(comment, post, authenticatedUser));
        }

        return PostDetailsResponseDto.builder()
                .postDetailsDto(postDetails)
                .comments(commentList)
                .build();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostDetailsDto {

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
        @JsonProperty(value = "is_written_by_user")
        private Boolean isWrittenByUser;

        @Builder
        public PostDetailsDto(String postTitle, String postContent, String postDate, String writerName,
                              int commentsCount, int likesCount, Boolean isWrittenByUser) {
            this.postTitle = postTitle;
            this.postContent = postContent;
            this.postDate = postDate;
            this.writerName = writerName;
            this.commentsCount = commentsCount;
            this.likesCount = likesCount;
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
                    .commentsCount(post.getCommentsCount())
                    .likesCount(post.getLikesCount())
                    .isWrittenByUser(isWrittenByUser)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CommentListDto {

        @JsonProperty(value = "comment_id")
        private Long commentId;
        @JsonProperty(value = "comment_content")
        private String commentContent;
        @JsonProperty(value = "comment_date")
        private String commentDate;
        @JsonProperty(value = "writer_name")
        private String writerName;
        @JsonProperty(value = "deleted")
        private Boolean isDeleted;
        @JsonProperty(value = "parent_comment_id")
        private Long parentCommentId;
        @JsonProperty(value = "likes_count")
        private int likesCount;
        @JsonProperty(value = "is_written_by_user")
        private Boolean isWrittenByUser;

        @Builder
        public CommentListDto(
                Long commentId, String commentContent, String commentDate, String writerName,
                Boolean isDeleted, Long parentCommentId, int likesCount, Boolean isWrittenByUser
        ) {
            this.commentId = commentId;
            this.commentContent = commentContent;
            this.commentDate = commentDate;
            this.writerName = writerName;
            this.isDeleted = isDeleted;
            this.parentCommentId = parentCommentId;
            this.likesCount = likesCount;
            this.isWrittenByUser = isWrittenByUser;
        }

        public static CommentListDto of(final Comment comment, Post post, final User authenticatedUser) {
            User commentWriter = comment.getWriter();
            boolean isWrittenByUser = authenticatedUser == commentWriter;

            String writerName = commentWriter.getName();
            User postWriter = post.getWriter();
            if (postWriter == commentWriter) {
                writerName = "글쓴이";
            }

            Comment parentComment = comment.getParentComment();
            Long parentCommentId = null;
            if (parentComment != null) {
                parentCommentId = parentComment.getId();
            }

            return CommentListDto.builder()
                    .commentId(comment.getId())
                    .commentContent(comment.getCommentContent())
                    .commentDate(comment.getCommentDate())
                    .writerName(writerName)
                    .isDeleted(comment.isDeleted())
                    .parentCommentId(parentCommentId)
                    .likesCount(comment.getLikesCount())
                    .isWrittenByUser(isWrittenByUser)
                    .build();
        }
    }
}
