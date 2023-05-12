package com.woopaca.knoo.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostListResponseDto {

    @JsonProperty(value = "total_pages")
    private int totalPages;

    private List<PostListDto> posts;

    @Builder
    public PostListResponseDto(int totalPages, List<PostListDto> posts) {
        this.totalPages = totalPages;
        this.posts = posts;
    }

    public static PostListResponseDto from(final Page<Post> postPage) {
        List<PostListDto> postList = new ArrayList<>();
        for (Post post : postPage) {
            postList.add(PostListDto.from(post));
        }
        return PostListResponseDto.builder()
                .totalPages(postPage.getTotalPages())
                .posts(postList)
                .build();
    }
}
