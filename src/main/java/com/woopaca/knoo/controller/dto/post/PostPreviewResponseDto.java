package com.woopaca.knoo.controller.dto.post;

import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.value.PostCategory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPreviewResponseDto {

    private PostCategory category;
    private List<PostListDto> posts;

    @Builder
    public PostPreviewResponseDto(PostCategory category, List<PostListDto> posts) {
        this.category = category;
        this.posts = posts;
    }

    public static PostPreviewResponseDto of(final PostCategory postCategory, final Page<Post> postPage) {
        List<PostListDto> postList = postPage.stream()
                .map(PostListDto::from)
                .collect(toList());

        return PostPreviewResponseDto.builder()
                .category(postCategory)
                .posts(postList)
                .build();
    }
}
