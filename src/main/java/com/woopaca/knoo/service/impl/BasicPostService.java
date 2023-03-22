package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.config.jwt.JwtUtils;
import com.woopaca.knoo.controller.post.dto.WritePostRequestDto;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.service.PostService;
import com.woopaca.knoo.service.dto.PostPreviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicPostService implements PostService {

    private final PostRepository postRepository;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public Long writePost(final String authorization, final WritePostRequestDto writePostRequestDto) {
        String token = jwtUtils.resolveToken(authorization);
        User user = jwtUtils.getAuthenticationPrincipal(token);

        Post post = Post.from(writePostRequestDto);
        post.writePost(user);
        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    @Override
    public List<PostPreviewDto> userWritePostList(final User user, final Pageable pageable) {
        List<PostPreviewDto> userWritePosts = new ArrayList<>();

        List<Post> postListFive = postRepository.findByWriter(user, pageable);
        for (Post post : postListFive) {
            PostPreviewDto postPreviewDto = PostPreviewDto.of(post.getId(), post.getPostTitle());
            userWritePosts.add(postPreviewDto);
        }
        return userWritePosts;
    }

    @Override
    public List<PostPreviewDto> userCommentPostList(final User user, final Pageable pageable) {
        List<PostPreviewDto> userCommentPosts = new ArrayList<>();

        List<Post> postListFive = postRepository.findByCommentWriter(user, pageable);
        for (Post post : postListFive) {
            PostPreviewDto postPreviewDto = PostPreviewDto.of(post.getId(), post.getPostTitle());
            userCommentPosts.add(postPreviewDto);
        }
        return userCommentPosts;
    }

    @Override
    public List<PostPreviewDto> userLikePostList(User user, final Pageable pageable) {
        List<PostPreviewDto> userCommentPosts = new ArrayList<>();

        List<Post> postListFive = postRepository.findByLikeUser(user, pageable);
        for (Post post : postListFive) {
            PostPreviewDto postPreviewDto = PostPreviewDto.of(post.getId(), post.getPostTitle());
            userCommentPosts.add(postPreviewDto);
        }
        return userCommentPosts;
    }
}
