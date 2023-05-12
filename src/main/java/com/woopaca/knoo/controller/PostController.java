package com.woopaca.knoo.controller;

import com.woopaca.knoo.annotation.SignIn;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.PostDetailsResponseDto;
import com.woopaca.knoo.controller.dto.post.PostLikeResponseDto;
import com.woopaca.knoo.controller.dto.post.PostListResponseDto;
import com.woopaca.knoo.controller.dto.post.PostScrapResponseDto;
import com.woopaca.knoo.controller.dto.post.UpdatePostRequestDto;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.entity.attr.PostCategory;
import com.woopaca.knoo.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> writeNewPost(
            @SignIn final SignInUser signInUser,
            @RequestBody @Valid final WritePostRequestDto writePostRequestDto
    ) {
        Long postId = postService.writePost(signInUser, writePostRequestDto);
        return ResponseEntity.created(URI.create("/api/v1/posts/" + postId))
                .body("게시글 작성이 완료되었습니다.");
    }

    @GetMapping("/{category}")
    public ResponseEntity<PostListResponseDto> writtenPostList(
            @PathVariable("category") final PostCategory postCategory,
            @RequestParam("page") final int page
    ) {
        PostListResponseDto postListResponseDto = postService.postList(postCategory, page - 1);
        return ResponseEntity.ok().body(postListResponseDto);
    }

    @GetMapping("/{category}/{postId}")
    public ResponseEntity<PostDetailsResponseDto> postDetailsInfo(
            @SignIn final SignInUser signInUser,
            @PathVariable("postId") final Long postId, @PathVariable("category") String ignore
    ) {
        PostDetailsResponseDto postDetails = postService.postDetails(signInUser, postId);
        return ResponseEntity.ok().body(postDetails);
    }

    @PatchMapping
    public ResponseEntity<String> updatePostContents(
            @SignIn final SignInUser signInUser,
            @RequestParam("post_id") final Long postId,
            @RequestBody @Valid final UpdatePostRequestDto updatePostRequestDto
    ) {
        postService.updatePost(signInUser, postId, updatePostRequestDto);
        return ResponseEntity.ok().body("게시글 수정이 완료되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteOnePost(
            @SignIn final SignInUser signInUser,
            @RequestParam("post_id") final Long postId
    ) {
        postService.deletePost(signInUser, postId);
        return ResponseEntity.ok().body("게시글 삭제가 완료되었습니다.");
    }

    @PostMapping("/likes")
    public ResponseEntity<PostLikeResponseDto> likeOrCancelLikePost(
            @SignIn final SignInUser signInUser,
            @RequestParam("post_id") final Long postId
    ) {
        PostLikeResponseDto postLikeResponseDto = postService.changePostLike(signInUser, postId);
        return ResponseEntity.ok().body(postLikeResponseDto);
    }

    @PostMapping("/scraps")
    public ResponseEntity<PostScrapResponseDto> scrapOrCancelScrapPost(
            @SignIn final SignInUser signInUser,
            @RequestParam("post_id") final Long postId
    ) {
        PostScrapResponseDto postScrapResponseDto = postService.changePostScrap(signInUser, postId);
        return ResponseEntity.ok().body(postScrapResponseDto);
    }
}
