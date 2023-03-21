package com.woopaca.knoo.entity;

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
    @Column(nullable = false)
    private String commentContent;
    @Column(nullable = false)
    private String commentDate;
    private Integer commentBundle;
    private Integer commentDepth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> commentLikes = new ArrayList<>();

    @Builder
    public Comment(String commentContent, String commentDate, Integer commentBundle, Integer commentDepth) {
        this.commentContent = commentContent;
        this.commentDate = commentDate;
        this.commentBundle = commentBundle;
        this.commentDepth = commentDepth;
    }

    public void writeComment(User writer, Post post) {
        this.writer = writer;
        this.post = post;
        writer.getComments().add(this);
        post.getComments().add(this);
    }
}
