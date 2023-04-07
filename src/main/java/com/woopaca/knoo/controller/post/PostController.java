package com.woopaca.knoo.controller.post;

import com.woopaca.knoo.controller.post.dto.PostDetailsResponseDto;
import com.woopaca.knoo.controller.post.dto.PostListResponseDto;
import com.woopaca.knoo.controller.post.dto.UpdatePostRequestDto;
import com.woopaca.knoo.controller.post.dto.WritePostRequestDto;
import com.woopaca.knoo.entity.PostCategory;
import com.woopaca.knoo.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> writeNewPost(
            @RequestHeader(AUTHORIZATION) final String authorization,
            @RequestBody @Valid final WritePostRequestDto writePostRequestDto
    ) {
        Long postId = postService.writePost(authorization, writePostRequestDto);
        return ResponseEntity.created(URI.create("/api/v1/posts/" + postId))
                .body("게시글 작성이 완료되었습니다.");
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<PostListResponseDto>> writtenPostList(
            @PathVariable("category") final PostCategory postCategory
    ) {
        List<PostListResponseDto> postList = postService.postList(postCategory);
        return ResponseEntity.ok().body(postList);
    }

    @GetMapping("/{category}/{postId}")
    public ResponseEntity<PostDetailsResponseDto> postDetailsInfo(
            @RequestHeader(AUTHORIZATION) final String authorization,
            @PathVariable("postId") final Long postId, @PathVariable("category") String ignore
    ) {
        PostDetailsResponseDto postDetails = postService.postDetails(postId, authorization);
        return ResponseEntity.ok().body(postDetails);
    }

    @PatchMapping
    public ResponseEntity<String> updatePostContents(
            @RequestHeader(AUTHORIZATION) final String authorization,
            @RequestParam("post_id") final Long postId,
            @RequestBody @Valid final UpdatePostRequestDto updatePostRequestDto
    ) {
        postService.updatePost(authorization, postId, updatePostRequestDto);
        return ResponseEntity.ok().body("게시글 수정이 완료되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteOnePost(
            @RequestHeader(AUTHORIZATION) final String authorization,
            @RequestParam("post_id") final Long postId
    ) {
        postService.deletePost(authorization, postId);
        return ResponseEntity.ok().body("게시글 삭제가 완료되었습니다.");
    }
}
