package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.comment.CommentLikeResponseDto;
import com.woopaca.knoo.controller.dto.comment.WriteCommentRequestDto;
import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.CommentLike;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.exception.comment.impl.CommentNotFoundException;
import com.woopaca.knoo.exception.comment.impl.DeletedCommentException;
import com.woopaca.knoo.exception.comment.impl.InvalidReplyException;
import com.woopaca.knoo.exception.post.impl.PostNotFoundException;
import com.woopaca.knoo.exception.user.impl.InvalidUserException;
import com.woopaca.knoo.repository.CommentLikeRepository;
import com.woopaca.knoo.repository.CommentRepository;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.service.AuthService;
import com.woopaca.knoo.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicCommentService implements CommentService {

    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    @Override
    public Long writeComment(
            final SignInUser signInUser, final WriteCommentRequestDto writeCommentRequestDto,
            @Nullable final Long postId, @Nullable final Long commentId
    ) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);
        Comment comment = Comment.from(writeCommentRequestDto);

        if (postId != null) {
            Comment writtenComment = newComment(comment, postId, authenticatedUser);
            return writtenComment.getId();
        }
        if (commentId != null) {
            Comment writtenReply = newReply(comment, commentId, authenticatedUser);
            return writtenReply.getId();
        }

        return null;
    }

    private Comment newComment(final Comment comment, final Long postId, final User authenticatedUser) {
        Post post = getPostWithLock(postId);
        comment.writtenBy(authenticatedUser);
        comment.writeOn(post);
        return commentRepository.save(comment);
    }

    private Comment newReply(final Comment comment, final Long commentId, final User authenticatedUser) {
        Comment parentComment =
                commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (parentComment.getParentComment() != null) {
            throw new InvalidReplyException();
        }

        Post parentCommentPost = parentComment.getPost();
        Post post = getPostWithLock(parentCommentPost.getId());
        comment.writtenBy(authenticatedUser);
        comment.reply(parentComment, post);
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void deleteComment(final SignInUser signInUser, final Long commentId) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);
        Comment comment =
                commentRepository.findById(commentId).orElseThrow((CommentNotFoundException::new));
        validateWriterAuthority(comment, authenticatedUser);

        comment.delete();
    }

    private Post getPostWithLock(Long postId) {
        return postRepository.findPostById(postId).orElseThrow(PostNotFoundException::new);
    }

    private void validateWriterAuthority(final Comment comment, final User authenticatedUser) {
        if (comment.getWriter() != authenticatedUser) {
            throw new InvalidUserException();
        }
    }

    @Transactional
    @Override
    public CommentLikeResponseDto changeCommentLike(final SignInUser signInUser, final Long commentId) {
        User authenticatedUser = authService.getAuthenticatedUser(signInUser);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        validateDeletedComment(comment);

        Optional<CommentLike> commentLikeOptional =
                commentLikeRepository.findByCommentAndUser(comment, authenticatedUser);

        if (commentLikeOptional.isPresent()) {
            cancelLikeComment(comment, commentLikeOptional);
            return CommentLikeResponseDto.ofCancelLike(comment);
        }

        likecomment(authenticatedUser, comment);
        return CommentLikeResponseDto.ofLike(comment);
    }

    private void likecomment(User authenticatedUser, Comment comment) {
        commentLikeRepository.save(CommentLike.userLikeComment(comment, authenticatedUser));
        comment.like();
    }

    private void cancelLikeComment(Comment comment, Optional<CommentLike> commentLikeOptional) {
        CommentLike commentLike = commentLikeOptional.get();
        commentLikeRepository.delete(commentLike);
        comment.cancelLike();
    }

    private void validateDeletedComment(final Comment comment) {
        if (comment.isDeleted()) {
            throw new DeletedCommentException();
        }
    }
}
