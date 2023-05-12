package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.PostListDto;
import com.woopaca.knoo.controller.dto.user.UserInfoResponseDto;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.service.AuthService;
import com.woopaca.knoo.service.PostService;
import com.woopaca.knoo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserService implements UserService {

    private final AuthService authService;
    private final PostService postService;

    @Override
    public UserInfoResponseDto userInfo(final SignInUser signInUser) {
        User user = authService.getAuthenticatedUser(signInUser);

        List<PostListDto> writePostList = postService.userWritePostList(user, PageRequest.of(0, 5));
        List<PostListDto> commentPostList = postService.userCommentPostList(user, PageRequest.of(0, 5));
        List<PostListDto> likePostList = postService.userLikePostList(user, PageRequest.of(0, 5));

        return UserInfoResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .writePosts(writePostList)
                .commentPosts(commentPostList)
                .likePosts(likePostList)
                .build();
    }
}
