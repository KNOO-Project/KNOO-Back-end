package com.woopaca.knoo.entity;

import com.woopaca.knoo.controller.dto.post.UpdatePostRequestDto;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.entity.value.PostCategory;
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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedEntityGraph(
        name = "Post.writer",
        attributeNodes = @NamedAttributeNode(value = "writer")
)
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "post_title", nullable = false)
    private String postTitle;

    @Column(name = "post_content", nullable = false)
    private String postContent;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "post_category", nullable = false)
    private PostCategory postCategory;

    @Column(name = "post_date", nullable = false)
    private LocalDateTime postDate;

    @Column(name = "anonymous", nullable = false)
    private boolean isAnonymous;

    @Column(name = "comments_count", nullable = false)
    private int commentsCount;

    @Column(name = "likes_count", nullable = false)
    private int likesCount;

    @Column(name = "scraps_count", nullable = false)
    private int scrapsCount;

    @JoinColumn(name = "thumbnail")
    @OneToOne(fetch = FetchType.LAZY)
    private Image thumbnail;

    @Column(name = "images_count")
    private int imagesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @OneToMany(mappedBy = "post", cascade = REMOVE)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = REMOVE)
    private List<Scrap> scraps = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = {PERSIST, MERGE, REMOVE})
    private List<Image> images = new ArrayList<>();

    @Builder
    public Post(String postTitle, String postContent, PostCategory postCategory,
                LocalDateTime postDate, boolean isAnonymous, int scrapsCount) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postCategory = postCategory;
        this.postDate = postDate;
        this.isAnonymous = isAnonymous;
        this.scrapsCount = scrapsCount;
    }

    public static Post from(final WritePostRequestDto writePostRequestDto) {
        return Post.builder()
                .postTitle(writePostRequestDto.getPostTitle())
                .postContent(writePostRequestDto.getPostContent())
                .postCategory(writePostRequestDto.getPostCategory())
                .postDate(LocalDateTime.now())
                .isAnonymous(writePostRequestDto.getIsAnonymous())
                .build();
    }

    public void writtenBy(User writer) {
        this.writer = writer;
        writer.getPosts().add(this);
    }

    public void commentWritten(Comment comment) {
        this.comments.add(comment);
        commentsCount++;
    }

    public void update(final UpdatePostRequestDto updatePostRequestDto) {
        postTitle = updatePostRequestDto.getPostTitle();
        postContent = updatePostRequestDto.getPostContent();
        isAnonymous = updatePostRequestDto.getIsAnonymous();
    }

    public void like() {
        likesCount++;
    }

    public void cancelLike() {
        likesCount--;
    }

    public void scrap() {
        scrapsCount++;
    }

    public void cancelScrap() {
        scrapsCount--;
    }

    public void updateThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void registerImage(Image image) {
        images.add(image);
        imagesCount++;
    }
}
