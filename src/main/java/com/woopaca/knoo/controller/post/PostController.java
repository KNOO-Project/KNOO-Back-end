package com.woopaca.knoo.controller.post;

import com.woopaca.knoo.controller.post.dto.WritePostRequestDto;
import com.woopaca.knoo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> writeNewPost(
            @RequestHeader("Authorization") final String authorization,
            @RequestBody @Valid final WritePostRequestDto writePostRequestDto
    ) {
        Long postId = postService.writePost(authorization, writePostRequestDto);
        return ResponseEntity.created(URI.create("/posts/" + postId))
                .body("게시글 작성이 완료되었습니다.");
    }
}
