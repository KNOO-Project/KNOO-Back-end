package com.woopaca.knoo.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @Column(name = "post_like_date", nullable = false)
    private LocalDateTime postLikeDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public PostLike(LocalDateTime postLikeDate, User user, Post post) {
        this.postLikeDate = postLikeDate;
        this.user = user;
        this.post = post;
    }

    public static PostLike userLikePost(User user, Post post) {
        PostLike postLike = new PostLike(LocalDateTime.now(), user, post);
        user.getPostLikes().add(postLike);
        post.getPostLikes().add(postLike);
        return postLike;
    }
}
