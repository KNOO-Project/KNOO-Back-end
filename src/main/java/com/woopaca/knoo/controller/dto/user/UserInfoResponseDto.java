package com.woopaca.knoo.controller.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.controller.dto.post.PostListDto;
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

    private List<PostListDto> writePosts;

    private List<PostListDto> commentPosts;

    private List<PostListDto> likePosts;

    @Builder
    public UserInfoResponseDto(String name, String email, List<PostListDto> writePosts,
                               List<PostListDto> commentPosts, List<PostListDto> likePosts) {
        this.name = name;
        this.email = email;
        this.writePosts = writePosts;
        this.commentPosts = commentPosts;
        this.likePosts = likePosts;
    }
}
