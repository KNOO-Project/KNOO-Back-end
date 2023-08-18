package com.woopaca.knoo.controller;

import com.woopaca.knoo.controller.dto.home.HomePostListResponse;
import com.woopaca.knoo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class HomeController {

    private final PostService postService;

    @GetMapping("/home")
    public ResponseEntity<List<HomePostListResponse>> homePostList() {
        List<HomePostListResponse> homePostListResponse = postService.getPopularAndRecentPosts();
        return ResponseEntity.ok().body(homePostListResponse);
    }
}
