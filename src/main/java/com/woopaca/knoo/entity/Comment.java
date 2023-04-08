package com.woopaca.knoo.entity;

import com.woopaca.knoo.controller.comment.dto.WriteCommentRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @Column(name = "comment_date", nullable = false)
    private String commentDate;

    @Column(name = "likes_count", nullable = false)
    private int likesCount;

    @Column(name = "deleted", nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> commentLikes = new ArrayList<>();

    @Builder
    public Comment(String commentContent, String commentDate) {
        this.commentContent = commentContent;
        this.commentDate = commentDate;
    }

    public static Comment from(final WriteCommentRequestDto writeCommentRequestDto) {
        String commentDate = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        return Comment.builder()
                .commentContent(writeCommentRequestDto.getCommentContent())
                .commentDate(commentDate)
                .build();
    }

    public void writeComment(User writer, Post post) {
        this.writer = writer;
        this.post = post;
        writer.getComments().add(this);
        post.getComments().add(this);
        post.commentWritten();
    }

    public void reply(final User writer, final Comment parentComment) {
        this.parentComment = parentComment;
        this.writer = writer;
        writer.getComments().add(this);

        Post post = parentComment.getPost();
        this.post = post;
        post.getComments().add(this);

        post.commentWritten();
    }

    public void delete() {
        isDeleted = true;
    }
}
