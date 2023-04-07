package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.config.jwt.JwtUtils;
import com.woopaca.knoo.controller.comment.dto.WriteCommentRequestDto;
import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.exception.comment.impl.CommentNotFoundException;
import com.woopaca.knoo.exception.post.impl.PostNotFoundException;
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
        String token = jwtUtils.resolveToken(authorization);
        User authenticatedUser = jwtUtils.getAuthenticationPrincipal(token);
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
}
