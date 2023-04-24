package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.PostDetailsResponseDto;
import com.woopaca.knoo.controller.dto.post.PostListResponseDto;
import com.woopaca.knoo.controller.dto.post.UpdatePostRequestDto;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.controller.dto.user.PostPreviewDto;
import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.PostCategory;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.exception.post.impl.PostCategoryNotFoundException;
import com.woopaca.knoo.exception.post.impl.PostNotFoundException;
import com.woopaca.knoo.exception.user.impl.InvalidUserException;
import com.woopaca.knoo.repository.CommentRepository;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.service.AuthService;
import com.woopaca.knoo.service.PostService;
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

    private final AuthService authService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Long writePost(final SignInUser signInUser, final WritePostRequestDto writePostRequestDto) {
        User authentcatedUser = authService.getAuthenticatedUser(signInUser);
        Post post = Post.from(writePostRequestDto);
        post.writtenBy(authentcatedUser);

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
    public PostDetailsResponseDto postDetails(final SignInUser signInUser, final Long postId) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<Comment> comments = commentRepository.findByPost(post);

        return PostDetailsResponseDto.of(post, comments, authenticatedUser);
    }

    @Transactional
    @Override
    public void updatePost(final SignInUser signInUser,
                           final Long postId, final UpdatePostRequestDto updatePostRequestDto) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        validateWriterAuthority(post, authenticatedUser);

        post.update(updatePostRequestDto);
    }

    @Transactional
    @Override
    public void deletePost(final SignInUser signInUser, final Long postId) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        validateWriterAuthority(post, authenticatedUser);

        postRepository.delete(post);
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
            PostPreviewDto postPreviewDto = PostPreviewDto.from(post);
            postPreviewList.add(postPreviewDto);
        }
    }

    private void validateWriterAuthority(final Post post, final User authenticatedUser) {
        if (post.getWriter() != authenticatedUser) {
            throw new InvalidUserException();
        }
    }
}
