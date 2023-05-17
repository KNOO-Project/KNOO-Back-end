package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.PostDetailsResponseDto;
import com.woopaca.knoo.controller.dto.post.PostLikeResponseDto;
import com.woopaca.knoo.controller.dto.post.PostListDto;
import com.woopaca.knoo.controller.dto.post.PostListResponseDto;
import com.woopaca.knoo.controller.dto.post.PostScrapResponseDto;
import com.woopaca.knoo.controller.dto.post.PostSearchRequestDto;
import com.woopaca.knoo.controller.dto.post.SearchCondition;
import com.woopaca.knoo.controller.dto.post.UpdatePostRequestDto;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
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

    List<PostListDto> userWritePostList(final User user, final Pageable pageable);

    List<PostListDto> userCommentPostList(final User user, final Pageable pageable);

    List<PostListDto> userLikePostList(final User user, final Pageable pageable);

    PostListResponseDto searchPosts(final PostSearchRequestDto postSearchRequestDto);

    PostListResponseDto searchUserScrapPosts(
            final SignInUser signInUser, final SearchCondition searchCondition, final String keyword, final int page);
}
