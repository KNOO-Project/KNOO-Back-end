package com.woopaca.knoo.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.common.DateFormatter;
import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.Image;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetailsResponseDto {

    @JsonProperty(value = "post")
    private PostDetailsDto postDetailsDto;

    private List<CommentListDto> comments;

    @Builder
    public PostDetailsResponseDto(PostDetailsDto postDetailsDto, List<CommentListDto> comments) {
        this.postDetailsDto = postDetailsDto;
        this.comments = comments;
    }

    public static PostDetailsResponseDto of(final Post post, final List<Comment> comments, final User authenticatedUser) {
        PostDetailsDto postDetails = PostDetailsDto.of(post, authenticatedUser);

        List<CommentListDto> commentList = comments.stream()
                .map(comment -> CommentListDto.of(comment, post, authenticatedUser))
                .collect(toList());

        return PostDetailsResponseDto.builder()
                .postDetailsDto(postDetails)
                .comments(commentList)
                .build();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostDetailsDto {

        private String postTitle;

        private String postContent;

        private String postCategory;

        private String postDate;

        private String writerName;

        private int commentsCount;

        private int likesCount;

        private int scrapsCount;

        private Boolean isWrittenByUser;

        private Boolean liked;

        private Boolean scrapped;

        private List<String> images;

        @Builder
        public PostDetailsDto(
                String postTitle, String postContent, String postCategory, String postDate,
                String writerName, int commentsCount, int likesCount, int scrapsCount,
                Boolean isWrittenByUser, Boolean liked, Boolean scrapped, List<String> images
        ) {
            this.postTitle = postTitle;
            this.postContent = postContent;
            this.postCategory = postCategory;
            this.postDate = postDate;
            this.writerName = writerName;
            this.commentsCount = commentsCount;
            this.likesCount = likesCount;
            this.scrapsCount = scrapsCount;
            this.isWrittenByUser = isWrittenByUser;
            this.liked = liked;
            this.scrapped = scrapped;
            this.images = images;
        }

        public static PostDetailsDto of(final Post post, final User authenticatedUser) {
            User writer = post.getWriter();
            String writerName = post.isAnonymous() ? "KNOOER" : writer.getName();
            boolean isWrittenByUser = writer == authenticatedUser;

            boolean liked = isLiked(post, authenticatedUser);
            boolean scrapped = isScrapped(post, authenticatedUser);

            String formattedDate =
                    post.getPostDate().format(DateFormatter.getFormatter());
            return PostDetailsDto.builder()
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .postCategory(post.getPostCategory().getCategoryName())
                    .postDate(formattedDate)
                    .writerName(writerName)
                    .commentsCount(post.getCommentsCount())
                    .likesCount(post.getLikesCount())
                    .scrapsCount(post.getScrapsCount())
                    .isWrittenByUser(isWrittenByUser)
                    .liked(liked)
                    .scrapped(scrapped)
                    .images(post.getImages().stream()
                            .map(Image::getImageUrl)
                            .collect(toList()))
                    .build();
        }

        private static boolean isLiked(Post post, User authenticatedUser) {
            return post.getPostLikes().stream()
                    .anyMatch(postLike -> postLike.getUser().equals(authenticatedUser));
        }

        private static boolean isScrapped(Post post, User authenticatedUser) {
            return post.getScraps().stream()
                    .anyMatch(scrap -> scrap.getUser().equals(authenticatedUser));
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CommentListDto {

        private Long commentId;

        private String commentContent;

        private String commentDate;

        private String writerName;

        private Boolean isDeleted;

        private Long parentCommentId;

        private int likesCount;

        private Boolean isWrittenByUser;

        private Boolean liked;

        @Builder
        public CommentListDto(
                Long commentId, String commentContent, String commentDate, String writerName,
                Boolean isDeleted, Long parentCommentId, int likesCount, Boolean isWrittenByUser, Boolean liked
        ) {
            this.commentId = commentId;
            this.commentContent = commentContent;
            this.commentDate = commentDate;
            this.writerName = writerName;
            this.isDeleted = isDeleted;
            this.parentCommentId = parentCommentId;
            this.likesCount = likesCount;
            this.isWrittenByUser = isWrittenByUser;
            this.liked = liked;
        }

        public static CommentListDto of(final Comment comment, Post post, final User authenticatedUser) {
            if (comment.isDeleted()) {
                return deletedComment(comment, post, authenticatedUser);
            }
            return normalComment(comment, post, authenticatedUser);
        }

        private static CommentListDto normalComment(final Comment comment, final Post post,
                                                    final User authenticatedUser) {
            Long parentCommentId = getParentCommentId(comment);

            User commentWriter = comment.getWriter();
            boolean isWrittenByUser = authenticatedUser == commentWriter;
            String writerName = getDisplayWriterName(post, commentWriter);

            boolean liked = comment.getCommentLikes().stream()
                    .anyMatch(commentLike -> commentLike.getUser().equals(authenticatedUser));

            String formattedDate =
                    comment.getCommentDate().format(DateFormatter.getFormatter());
            return CommentListDto.builder()
                    .commentId(comment.getId())
                    .commentContent(comment.getCommentContent())
                    .commentDate(formattedDate)
                    .writerName(writerName)
                    .isDeleted(comment.isDeleted())
                    .parentCommentId(parentCommentId)
                    .likesCount(comment.getLikesCount())
                    .isWrittenByUser(isWrittenByUser)
                    .liked(liked)
                    .build();
        }

        private static CommentListDto deletedComment(final Comment comment, final Post post,
                                                     final User authenticatedUser) {
            Long parentCommentId = getParentCommentId(comment);

            return CommentListDto.builder()
                    .commentId(comment.getId())
                    .commentContent("삭제된 댓글입니다.")
                    .commentDate("")
                    .writerName("")
                    .isDeleted(comment.isDeleted())
                    .parentCommentId(parentCommentId)
                    .isWrittenByUser(false)
                    .build();
        }

        private static Long getParentCommentId(Comment comment) {
            Comment parentComment = comment.getParentComment();
            Long parentCommentId = null;
            if (parentComment != null) {
                parentCommentId = parentComment.getId();
            }

            return parentCommentId;
        }

        private static String getDisplayWriterName(Post post, User commentWriter) {
            String writerName = commentWriter.getName();
            User postWriter = post.getWriter();
            if (postWriter == commentWriter) {
                writerName = "글쓴이";
            }

            return writerName;
        }
    }
}
