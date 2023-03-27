package com.woopaca.knoo.controller.post;

import com.woopaca.knoo.controller.post.dto.PostDetailsResponseDto;
import com.woopaca.knoo.controller.post.dto.PostListResponseDto;
import com.woopaca.knoo.controller.post.dto.WritePostRequestDto;
import com.woopaca.knoo.entity.PostCategory;
import com.woopaca.knoo.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @PathVariable(value = "category") final PostCategory postCategory
    ) {
        List<PostListResponseDto> postList = postService.postList(postCategory);
        return ResponseEntity.ok().body(postList);
    }

    @GetMapping("/{category}/{postId}")
    public ResponseEntity<PostDetailsResponseDto> postDetailsInfo(
            @RequestHeader(AUTHORIZATION) final String authorization,
            @PathVariable("postId") final Long postId,
            @PathVariable("category") final String postCategory) {
        PostDetailsResponseDto postDetails = postService.postDetails(postId, authorization);
        return ResponseEntity.ok().body(postDetails);
    }
}
