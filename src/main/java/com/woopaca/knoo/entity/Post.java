package com.woopaca.knoo.entity;

import com.woopaca.knoo.controller.post.dto.WritePostRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @Column(nullable = false)
    private String postTitle;
    @Column(nullable = false)
    private String postContent;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PostCategory postCategory;
    @Column(nullable = false)
    private String postDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @OneToMany(mappedBy = "post")
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(String postTitle, String postContent, PostCategory postCategory, String postDate) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postCategory = postCategory;
        this.postDate = postDate;
    }

    public static Post from(final WritePostRequestDto writePostRequestDto) {
        String postDate = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return Post.builder()
                .postTitle(writePostRequestDto.getPostTitle())
                .postContent(writePostRequestDto.getPostContent())
                .postCategory(writePostRequestDto.getPostCategory())
                .postDate(postDate)
                .build();
    }

    public void writePost(User writer) {
        this.writer = writer;
        writer.getPosts().add(this);
    }
}
