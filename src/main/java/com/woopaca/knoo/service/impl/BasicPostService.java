package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.config.jwt.JwtUtils;
import com.woopaca.knoo.controller.post.dto.PostDetailsResponseDto;
import com.woopaca.knoo.controller.post.dto.PostListResponseDto;
import com.woopaca.knoo.controller.post.dto.UpdatePostRequestDto;
import com.woopaca.knoo.controller.post.dto.WritePostRequestDto;
import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.PostCategory;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.exception.post.impl.PostCategoryNotFoundException;
import com.woopaca.knoo.exception.post.impl.PostNotFoundException;
import com.woopaca.knoo.exception.user.impl.InvalidUserException;
import com.woopaca.knoo.repository.CommentRepository;
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
    private final CommentRepository commentRepository;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public Long writePost(final String authorization, final WritePostRequestDto writePostRequestDto) {
        String token = jwtUtils.resolveToken(authorization);
        User authentcatedUser = jwtUtils.getAuthenticationPrincipal(token);

        Post post = Post.from(writePostRequestDto);
        post.writePost(authentcatedUser);
        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    @Override
    public List<PostListResponseDto> postList(final PostCategory postCategory) {
        if (postCategory == null) {
            throw new PostCategoryNotFoundException();
        }

        List<PostListResponseDto> postList = new ArrayList<>();
        List<Post> posts = postRepository.findByPostCategoryOrderByPostDate(postCategory);
        for (Post post : posts) {
            postList.add(PostListResponseDto.from(post));
        }

        return postList;
    }

    @Override
    public PostDetailsResponseDto postDetails(final Long postId, final String authorization) {
        String token = jwtUtils.resolveToken(authorization);
        User authenticatedUser = jwtUtils.getAuthenticationPrincipal(token);

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<Comment> comments = commentRepository.findByPost(post);

        return PostDetailsResponseDto.of(post, comments, authenticatedUser);
    }

    @Override
    @Transactional
    public void postUpdate(final String authorization,
                           final Long postId, final UpdatePostRequestDto updatePostRequestDto) {
        String token = jwtUtils.resolveToken(authorization);
        User authenticatedUser = jwtUtils.getAuthenticationPrincipal(token);

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if (post.getWriter() != authenticatedUser) {
            throw new InvalidUserException();
        }

        post.update(updatePostRequestDto);
    }

    @Override
    public List<PostPreviewDto> userWritePostList(final User user, final Pageable pageable) {
        List<PostPreviewDto> userWritePosts = new ArrayList<>();
        List<Post> postListFive = postRepository.findByWriter(user, pageable);
        postsToPostPreviewList(postListFive, userWritePosts);
        return userWritePosts;
    }

    @Override
    public List<PostPreviewDto> userCommentPostList(final User user, final Pageable pageable) {
        List<PostPreviewDto> userCommentPosts = new ArrayList<>();
        List<Post> postListFive = postRepository.findByCommentWriter(user, pageable);
        postsToPostPreviewList(postListFive, userCommentPosts);
        return userCommentPosts;
    }

    @Override
    public List<PostPreviewDto> userLikePostList(User user, final Pageable pageable) {
        List<PostPreviewDto> userLikePosts = new ArrayList<>();
        List<Post> postListFive = postRepository.findByLikeUser(user, pageable);
        postsToPostPreviewList(postListFive, userLikePosts);
        return userLikePosts;
    }

    private void postsToPostPreviewList(List<Post> posts, List<PostPreviewDto> postPreviewList) {
        for (Post post : posts) {
            PostPreviewDto postPreviewDto = PostPreviewDto.of(post.getId(), post.getPostTitle());
            postPreviewList.add(postPreviewDto);
        }
    }
}
