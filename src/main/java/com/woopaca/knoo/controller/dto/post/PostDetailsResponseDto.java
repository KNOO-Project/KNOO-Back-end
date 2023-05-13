package com.woopaca.knoo.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.CommentLike;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.PostLike;
import com.woopaca.knoo.entity.Scrap;
import com.woopaca.knoo.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

        @JsonProperty(value = "post_category")
        private String postCategory;

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

        @JsonProperty(value = "is_written_by_user")
        private Boolean isWrittenByUser;

        private Boolean liked;

        private Boolean scrapped;

        @Builder
        public PostDetailsDto(String postTitle, String postContent, String postCategory, String postDate, String writerName,
                              int commentsCount, int likesCount, int scrapsCount, Boolean isWrittenByUser, Boolean liked, Boolean scrapped) {
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
        }

        public static PostDetailsDto of(final Post post, final User authenticatedUser) {
            User writer = post.getWriter();
            String writerName = post.isAnonymous() ? "KNOOER" : writer.getName();
            boolean isWrittenByUser = writer == authenticatedUser;

            boolean liked = isLiked(post, authenticatedUser);
            boolean scrapped = isScrapped(post, authenticatedUser);

            String formattedDate =
                    post.getPostDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
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
                    .build();
        }

        private static boolean isLiked(Post post, User authenticatedUser) {
            boolean liked = false;
            List<PostLike> postLikeList = post.getPostLikes();
            for (PostLike postLike : postLikeList) {
                if (postLike.getUser() == authenticatedUser) {
                    liked = true;
                    break;
                }
            }
            return liked;
        }

        private static boolean isScrapped(Post post, User authenticatedUser) {
            boolean scrapped = false;
            List<Scrap> scrapList = post.getScraps();
            for (Scrap scrap : scrapList) {
                if (scrap.getUser() == authenticatedUser) {
                    scrapped = true;
                    break;
                }
            }
            return scrapped;
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

            boolean liked = false;
            List<CommentLike> commentLikeList = comment.getCommentLikes();
            for (CommentLike commentLike : commentLikeList) {
                if (commentLike.getUser() == authenticatedUser) {
                    liked = true;
                    break;
                }
            }

            String formattedDate =
                    comment.getCommentDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
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
