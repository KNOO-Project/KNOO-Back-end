package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.post.dto.PostListResponseDto;
import com.woopaca.knoo.controller.post.dto.WritePostRequestDto;
import com.woopaca.knoo.entity.PostCategory;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.service.dto.PostPreviewDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    Long writePost(final String authorization, final WritePostRequestDto writePostRequestDto);

    List<PostListResponseDto> postList(final PostCategory postCategory);

    List<PostPreviewDto> userWritePostList(final User user, final Pageable pageable);

    List<PostPreviewDto> userCommentPostList(final User user, final Pageable pageable);

    List<PostPreviewDto> userLikePostList(final User user, final Pageable pageable);
}
