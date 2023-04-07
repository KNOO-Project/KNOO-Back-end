package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.config.jwt.JwtUtils;
import com.woopaca.knoo.controller.comment.dto.WriteCommentRequestDto;
import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.exception.comment.impl.CommentNotFoundException;
import com.woopaca.knoo.exception.post.impl.PostNotFoundException;
import com.woopaca.knoo.exception.user.impl.InvalidUserException;
import com.woopaca.knoo.repository.CommentRepository;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicCommentService implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    @Override
    public Long writeComment(
            final WriteCommentRequestDto writeCommentRequestDto, @Nullable final Long postId,
            @Nullable final Long commentId, final String authorization
    ) {
        User authenticatedUser = getAuthenticatedUser(authorization);
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

    @Transactional
    @Override
    public void deleteComment(final String authorization, final Long commentId) {
        User authenticatedUser = getAuthenticatedUser(authorization);
        Comment comment =
                commentRepository.findById(commentId).orElseThrow((CommentNotFoundException::new));
        validateWriterAuthority(comment, authenticatedUser);

        comment.delete();
    }

    private Comment newComment(final Comment comment, final Long postId,
                               final User authenticatedUser) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        comment.writeComment(authenticatedUser, post);
        return commentRepository.save(comment);
    }

    private Comment newReply(final Comment comment, final Long commentId,
                             final User authenticatedUser) {
        Comment parentComment =
                commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.reply(authenticatedUser, parentComment);
        return commentRepository.save(comment);
    }

    private User getAuthenticatedUser(final String authorization) {
        String token = jwtUtils.resolveToken(authorization);
        return jwtUtils.getAuthenticationPrincipal(token);
    }

    private void validateWriterAuthority(final Comment comment, final User authenticatedUser) {
        if (comment.getWriter() != authenticatedUser) {
            throw new InvalidUserException();
        }
    }
}
