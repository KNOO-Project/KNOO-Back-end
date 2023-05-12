package com.woopaca.knoo.entity;

import com.woopaca.knoo.controller.dto.comment.WriteCommentRequestDto;
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
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @Column(name = "comment_date", nullable = false)
    private LocalDateTime commentDate;

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
    public Comment(String commentContent, LocalDateTime commentDate) {
        this.commentContent = commentContent;
        this.commentDate = commentDate;
    }

    public static Comment from(final WriteCommentRequestDto writeCommentRequestDto) {
        return Comment.builder()
                .commentContent(writeCommentRequestDto.getCommentContent())
                .commentDate(LocalDateTime.now())
                .build();
    }

    public void writtenBy(User writer) {
        this.writer = writer;
        writer.getComments().add(this);
    }

    public void writeOn(Post post) {
        this.post = post;
        post.commentWritten(this);
    }

    public void reply(final Comment parentComment, Post post) {
        this.parentComment = parentComment;

        this.post = post;
        post.commentWritten(this);
    }

    public void delete() {
        isDeleted = true;
    }

    public void like() {
        likesCount++;
    }

    public void cancelLike() {
        likesCount--;
    }
}
