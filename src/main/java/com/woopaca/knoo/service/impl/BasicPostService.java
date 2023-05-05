package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.PostDetailsResponseDto;
import com.woopaca.knoo.controller.dto.post.PostLikeResponseDto;
import com.woopaca.knoo.controller.dto.post.PostListResponseDto;
import com.woopaca.knoo.controller.dto.post.UpdatePostRequestDto;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.controller.dto.user.PostPreviewDto;
import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.PostCategory;
import com.woopaca.knoo.entity.PostLike;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.exception.post.impl.InvalidPostPageException;
import com.woopaca.knoo.exception.post.impl.PageCountExceededException;
import com.woopaca.knoo.exception.post.impl.PostCategoryNotFoundException;
import com.woopaca.knoo.exception.post.impl.PostNotFoundException;
import com.woopaca.knoo.exception.user.impl.InvalidUserException;
import com.woopaca.knoo.repository.CommentRepository;
import com.woopaca.knoo.repository.PostLikeRepository;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.service.AuthService;
import com.woopaca.knoo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicPostService implements PostService {

    public static final int PAGE_SIZE = 20;

    private final AuthService authService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

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
    public PostListResponseDto postList(final PostCategory postCategory, final int page) {
        if (postCategory == null) {
            throw new PostCategoryNotFoundException();
        }
        if (page < 0) {
            throw new InvalidPostPageException();
        }

        PageRequest pageRequest =
                PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
        Page<Post> postPage = postRepository.findByPostCategory(postCategory, pageRequest);
        if (postPage.getTotalPages() <= page) {
            throw new PageCountExceededException();
        }

        return PostListResponseDto.from(postPage);
    }

    @Override
    public PostDetailsResponseDto postDetails(final SignInUser signInUser, final Long postId) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<Comment> comments =
                commentRepository.findByPost(post);

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

    private void validateWriterAuthority(final Post post, final User authenticatedUser) {
        if (post.getWriter() != authenticatedUser) {
            throw new InvalidUserException();
        }
    }

    @Transactional
    @Override
    public PostLikeResponseDto changeLikesOnPost(final SignInUser signInUser, final Long postId) {
        User authentcatedUser = authService.getAuthenticatedUser(signInUser);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Optional<PostLike> postLikeOptional =
                postLikeRepository.findByPostAndUser(post, authentcatedUser);

        if (postLikeOptional.isPresent()) {
            unlikesPost(post, postLikeOptional);
            return PostLikeResponseDto.ofUnlike(post);
        }

        likesPost(post, authentcatedUser);
        return PostLikeResponseDto.ofLike(post);
    }

    private void likesPost(Post post, User authentcatedUser) {
        postLikeRepository.save(PostLike.userLikePost(post, authentcatedUser));
        post.likes();
    }

    private void unlikesPost(Post post, Optional<PostLike> postLikeOptional) {
        PostLike postLike = postLikeOptional.get();
        postLikeRepository.delete(postLike);
        post.unlikes();
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
}
