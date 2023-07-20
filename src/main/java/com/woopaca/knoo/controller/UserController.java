package com.woopaca.knoo.controller;

import com.woopaca.knoo.annotation.SignIn;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.PostListResponseDto;
import com.woopaca.knoo.controller.dto.post.PostSearchRequestDto;
import com.woopaca.knoo.controller.dto.post.UserPostsKind;
import com.woopaca.knoo.controller.dto.user.UserInfoResponseDto;
import com.woopaca.knoo.service.PostService;
import com.woopaca.knoo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping
    public ResponseEntity<UserInfoResponseDto> userProfile(@SignIn final SignInUser signInUser) {
        UserInfoResponseDto userInfoResponseDto = userService.userInfo(signInUser);
        return ResponseEntity.ok().body(userInfoResponseDto);
    }

    @GetMapping("/more")
    public ResponseEntity<PostListResponseDto> userWritePostList(
            @SignIn final SignInUser signInUser,
            @RequestParam("kind") final UserPostsKind userPostsKind, @RequestParam("page") final int page
    ) {
        PostListResponseDto postListResponseDto =
                userService.seeMoreUserPosts(signInUser, userPostsKind, page - 1);
        return ResponseEntity.ok().body(postListResponseDto);
    }

    @GetMapping("/scraps")
    public ResponseEntity<PostListResponseDto> userScrapPostList(
            @SignIn final SignInUser signInUser, @RequestParam("page") final int page
    ) {
        PostListResponseDto postListResponseDto = postService.scrapPostList(signInUser, page - 1);
        return ResponseEntity.ok().body(postListResponseDto);
    }

    @GetMapping("/scraps/search")
    public ResponseEntity<PostListResponseDto> searchUserScrapPosts(
            @SignIn final SignInUser signInUser, @Valid final PostSearchRequestDto postSearchRequestDto) {
        PostListResponseDto postListResponseDto = postService.searchUserScrapPosts(signInUser, postSearchRequestDto);
        return ResponseEntity.ok().body(postListResponseDto);
    }
}
