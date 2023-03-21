package com.woopaca.knoo.controller.user.dto;

import com.woopaca.knoo.service.dto.PostPreviewDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoResponseDto {

    private String name;
    private List<PostPreviewDto> writePosts;
    private List<PostPreviewDto> commentPosts;
    private List<PostPreviewDto> likePosts;

    @Builder
    public UserInfoResponseDto(String name, List<PostPreviewDto> writePosts,
                               List<PostPreviewDto> commentPosts, List<PostPreviewDto> likePosts) {
        this.name = name;
        this.writePosts = writePosts;
        this.commentPosts = commentPosts;
        this.likePosts = likePosts;
    }
}
