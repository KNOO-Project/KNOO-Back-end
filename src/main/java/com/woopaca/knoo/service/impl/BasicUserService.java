package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.config.jwt.JwtUtils;
import com.woopaca.knoo.controller.user.dto.UserInfoResponseDto;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.exception.user.impl.UserNotFoundException;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.PostService;
import com.woopaca.knoo.service.UserService;
import com.woopaca.knoo.controller.user.dto.PostPreviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PostService postService;

    @Override
    public UserInfoResponseDto userInfo(final String authorization) {
        String token = jwtUtils.resolveToken(authorization);
        String username = jwtUtils.getUsernameInToken(token);
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException());

        List<PostPreviewDto> writePostList = postService.userWritePostList(user, PageRequest.of(0, 5));
        List<PostPreviewDto> commentPostList = postService.userCommentPostList(user, PageRequest.of(0, 5));
        List<PostPreviewDto> likePostList = postService.userLikePostList(user, PageRequest.of(0, 5));

        return UserInfoResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .writePosts(writePostList)
                .commentPosts(commentPostList)
                .likePosts(likePostList)
                .build();
    }
}
