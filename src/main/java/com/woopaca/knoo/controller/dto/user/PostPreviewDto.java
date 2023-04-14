package com.woopaca.knoo.controller.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.PostCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostPreviewDto {

    @JsonProperty(value = "post_id")
    private Long postId;

    @JsonProperty(value = "post_title")
    private String postTitle;

    @JsonProperty(value = "post_category")
    private String postCategory;

    public static PostPreviewDto from(Post post) {
        PostCategory postCategory = post.getPostCategory();
        return new PostPreviewDto(
                post.getId(), post.getPostTitle(), postCategory.getCategoryName()
        );
    }
}