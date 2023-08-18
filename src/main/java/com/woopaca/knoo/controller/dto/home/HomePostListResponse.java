package com.woopaca.knoo.controller.dto.home;

import com.woopaca.knoo.controller.dto.post.PostListDto;
import com.woopaca.knoo.entity.Post;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HomePostListResponse {

    private final String postType;
    private final List<PostListDto> posts;

    @Builder
    protected HomePostListResponse(String postType, List<PostListDto> posts) {
        this.postType = postType;
        this.posts = posts;
    }

    public static HomePostListResponse of(String postType, Page<Post> posts) {
        return HomePostListResponse.builder()
                .postType(postType)
                .posts(posts.stream()
                        .map(PostListDto::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
