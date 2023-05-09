package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.*;
import com.woopaca.knoo.controller.dto.user.PostPreviewDto;
import com.woopaca.knoo.entity.*;
import com.woopaca.knoo.entity.attr.PostCategory;
import com.woopaca.knoo.exception.post.impl.InvalidPostPageException;
import com.woopaca.knoo.exception.post.impl.PageCountExceededException;
import com.woopaca.knoo.exception.post.impl.PostCategoryNotFoundException;
import com.woopaca.knoo.exception.post.impl.PostNotFoundException;
import com.woopaca.knoo.exception.user.impl.InvalidUserException;
import com.woopaca.knoo.repository.CommentRepository;
import com.woopaca.knoo.repository.PostLikeRepository;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.repository.ScrapRepository;
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
    private final ScrapRepository scrapRepository;

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
        validateArgument(postCategory, page);

        PageRequest pageRequest =
                PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "postDate"));
        Page<Post> postPage = postRepository.findByPostCategory(postCategory, pageRequest);
        validatePage(page, postPage);

        return PostListResponseDto.from(postPage);
    }

    @Override
    public PostListResponseDto scrapPostList(final SignInUser signInUser, final int page) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);
        if (page < 0) {
            throw new InvalidPostPageException();
        }

        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Post> postPage = postRepository.findUserScrapPosts(authenticatedUser, pageRequest);
        validatePage(page, postPage);

        return PostListResponseDto.from(postPage);
    }

    private static void validatePage(int page, Page<Post> postPage) {
        if (postPage.getTotalPages() != 0 && postPage.getTotalPages() <= page) {
            throw new PageCountExceededException();
        }
    }

    private static void validateArgument(PostCategory postCategory, int page) {
        if (postCategory == null) {
            throw new PostCategoryNotFoundException();
        }
        if (page < 0) {
            throw new InvalidPostPageException();
        }
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
    public PostLikeResponseDto changePostLike(final SignInUser signInUser, final Long postId) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);
        Post post = postRepository.findPostById(postId).orElseThrow(PostNotFoundException::new);
        Optional<PostLike> postLikeOptional = postLikeRepository.findByPostAndUser(post, authenticatedUser);

        if (postLikeOptional.isPresent()) {
            return cancelLikePost(post, postLikeOptional);
        }

        return likePost(post, authenticatedUser);
    }

    private PostLikeResponseDto likePost(Post post, User authentcatedUser) {
        postLikeRepository.save(PostLike.userLikePost(post, authentcatedUser));
        post.like();
        return PostLikeResponseDto.ofLike(post);
    }

    private PostLikeResponseDto cancelLikePost(Post post, Optional<PostLike> postLikeOptional) {
        PostLike postLike = postLikeOptional.get();
        postLikeRepository.delete(postLike);
        post.cancelLike();
        return PostLikeResponseDto.ofCancelLike(post);
    }

    @Transactional
    @Override
    public PostScrapResponseDto changePostScrap(final SignInUser signInUser, final Long postId) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);
        Post post = postRepository.findPostById(postId).orElseThrow(PostNotFoundException::new);
        Optional<Scrap> scrapOptional = scrapRepository.findByPostAndUser(post, authenticatedUser);

        if (scrapOptional.isPresent()) {
            return cancelScrapPost(post, scrapOptional);
        }

        return scrapPost(post, authenticatedUser);
    }

    private PostScrapResponseDto scrapPost(final Post post, final User authenticatedUser) {
        scrapRepository.save(Scrap.userScrapPost(post, authenticatedUser));
        post.scrap();
        return PostScrapResponseDto.ofScrap(post);
    }

    private PostScrapResponseDto cancelScrapPost(final Post post, final Optional<Scrap> scrapOptional) {
        Scrap scrap = scrapOptional.get();
        scrapRepository.delete(scrap);
        post.cancelScrap();
        return PostScrapResponseDto.ofCancelScrap(post);
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
    public List<PostPreviewDto> userLikePostList(final User user, final Pageable pageable) {
        List<PostPreviewDto> userLikePosts = new ArrayList<>();
        List<Post> postListFive = postRepository.findByLikeUser(user, pageable);
        postsToPostPreviewList(postListFive, userLikePosts);
        return userLikePosts;
    }

    private void postsToPostPreviewList(final List<Post> posts, final List<PostPreviewDto> postPreviewList) {
        for (Post post : posts) {
            PostPreviewDto postPreviewDto = PostPreviewDto.from(post);
            postPreviewList.add(postPreviewDto);
        }
    }
}
