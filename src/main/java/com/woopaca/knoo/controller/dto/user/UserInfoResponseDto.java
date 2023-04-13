package com.woopaca.knoo.controller.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoResponseDto {

    private String name;

    private String email;

    @JsonProperty(value = "write_posts")
    private List<PostPreviewDto> writePosts;

    @JsonProperty(value = "comment_posts")
    private List<PostPreviewDto> commentPosts;

    @JsonProperty(value = "like_posts")
    private List<PostPreviewDto> likePosts;

    @Builder
    public UserInfoResponseDto(String name, String email, List<PostPreviewDto> writePosts,
                               List<PostPreviewDto> commentPosts, List<PostPreviewDto> likePosts) {
        this.name = name;
        this.email = email;
        this.writePosts = writePosts;
        this.commentPosts = commentPosts;
        this.likePosts = likePosts;
    }
}
