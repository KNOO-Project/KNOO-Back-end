package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.PostDetailsResponseDto;
import com.woopaca.knoo.controller.dto.post.PostListResponseDto;
import com.woopaca.knoo.controller.dto.post.UpdatePostRequestDto;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.controller.dto.user.PostPreviewDto;
import com.woopaca.knoo.entity.PostCategory;
import com.woopaca.knoo.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    Long writePost(final SignInUser signInUser, final WritePostRequestDto writePostRequestDto);

    PostListResponseDto postList(final PostCategory postCategory, final int page);

    PostDetailsResponseDto postDetails(final SignInUser signInUser, final Long postId);

    List<PostPreviewDto> userWritePostList(final User user, final Pageable pageable);

    List<PostPreviewDto> userCommentPostList(final User user, final Pageable pageable);

    List<PostPreviewDto> userLikePostList(final User user, final Pageable pageable);

    void updatePost(final SignInUser signInUser, final Long postId, final UpdatePostRequestDto updatePostRequestDto);

    void deletePost(final SignInUser signInUser, final Long postId);
}
