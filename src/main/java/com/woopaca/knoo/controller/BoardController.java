package com.woopaca.knoo.controller;

import com.woopaca.knoo.controller.dto.PageDto;
import com.woopaca.knoo.controller.dto.post.PostListResponseDto;
import com.woopaca.knoo.entity.value.PostCategory;
import com.woopaca.knoo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/boards")
@RestController
public class BoardController {

    private final PostService postService;

    @GetMapping("/{category}")
    public ResponseEntity<PostListResponseDto> writtenPostList(
            @PathVariable("category") final PostCategory postCategory,
            @ModelAttribute @Valid final PageDto pageDto
    ) {
        PostListResponseDto postListResponseDto = postService.postList(postCategory, pageDto.getPage() - 1);
        return ResponseEntity.ok().body(postListResponseDto);
    }
}
