package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.*;
import com.woopaca.knoo.controller.dto.user.PostPreviewDto;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.attr.PostCategory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    Long writePost(final SignInUser signInUser, final WritePostRequestDto writePostRequestDto);

    PostListResponseDto postList(final PostCategory postCategory, final int page);

    PostListResponseDto scrapPostList(final SignInUser signInUser, final int page);

    PostDetailsResponseDto postDetails(final SignInUser signInUser, final Long postId);

    void updatePost(final SignInUser signInUser, final Long postId, final UpdatePostRequestDto updatePostRequestDto);

    void deletePost(final SignInUser signInUser, final Long postId);

    PostLikeResponseDto changePostLike(final SignInUser signInUser, final Long postId);

    PostScrapResponseDto changePostScrap(final SignInUser signInUser, final Long postId);

    List<PostPreviewDto> userWritePostList(final User user, final Pageable pageable);

    List<PostPreviewDto> userCommentPostList(final User user, final Pageable pageable);

    List<PostPreviewDto> userLikePostList(final User user, final Pageable pageable);
}
