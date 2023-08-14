package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.PostListDto;
import com.woopaca.knoo.controller.dto.post.PostListResponseDto;
import com.woopaca.knoo.controller.dto.post.UserPostsKind;
import com.woopaca.knoo.controller.dto.user.UserInfoResponseDto;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.exception.post.impl.InvalidPostPageException;
import com.woopaca.knoo.exception.post.impl.PageCountExceededException;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.service.AuthService;
import com.woopaca.knoo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserService implements UserService {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int PREVIEW_PAGE_SIZE = 5;
    private final AuthService authService;
    private final PostRepository postRepository;

    @Override
    public UserInfoResponseDto userInfo(final SignInUser signInUser) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);

        PageRequest previewPageRequest = PageRequest.of(0, PREVIEW_PAGE_SIZE);
        Page<Post> writePostPage = postRepository.findByWriter(authenticatedUser, previewPageRequest);
        Page<Post> commentPostPage = postRepository.findByCommentWriter(authenticatedUser, previewPageRequest);
        Page<Post> likePostPage = postRepository.findByLikeUser(authenticatedUser, previewPageRequest);

        return UserInfoResponseDto.builder()
                .name(authenticatedUser.getName())
                .email(authenticatedUser.getEmail())
                .writePosts(postPageToPostPreviewList(writePostPage))
                .commentPosts(postPageToPostPreviewList(commentPostPage))
                .likePosts(postPageToPostPreviewList(likePostPage))
                .build();
    }

    @Override
    public PostListResponseDto seeMoreUserPosts(final SignInUser signInUser,
                                                final UserPostsKind userPostsKind, final int page) {
        if (page < 0) {
            throw new InvalidPostPageException();
        }

        User authenticatedUser = authService.getAuthenticatedUser(signInUser);

        Page<Post> postPage = null;
        PageRequest pageRequest = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        switch (userPostsKind) {
            case WRITE: {
                postPage = postRepository.findByWriter(authenticatedUser, pageRequest);
                break;
            }
            case COMMENT: {
                postPage = postRepository.findByCommentWriter(authenticatedUser, pageRequest);
                break;
            }
            case LIKE: {
                postPage = postRepository.findByLikeUser(authenticatedUser, pageRequest);
                break;
            }
        }
        validatePage(page, postPage);

        return PostListResponseDto.from(postPage);
    }

    private List<PostListDto> postPageToPostPreviewList(final Page<Post> postPage) {
        return postPage.stream()
                .map(PostListDto::from)
                .collect(toList());
    }

    private static void validatePage(final int page, final Page<Post> postPage) {
        if (postPage.getTotalPages() == 0 && page == 0) {
            return;
        }
        if (postPage.getTotalPages() <= page) {
            throw new PageCountExceededException();
        }
    }
}
