package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.config.jwt.JwtUtils;
import com.woopaca.knoo.controller.comment.dto.WriteCommentRequestDto;
import com.woopaca.knoo.entity.Comment;
import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.exception.post.impl.PostNotFoundException;
import com.woopaca.knoo.repository.CommentRepository;
import com.woopaca.knoo.repository.PostRepository;
import com.woopaca.knoo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicCommentService implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional

    public void writeComment(final WriteCommentRequestDto writeCommentRequestDto,
                             final Long postId, final String authorization) {
        String token = jwtUtils.resolveToken(authorization);
        User authentcatedUser = jwtUtils.getAuthenticationPrincipal(token);

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        Comment comment = Comment.from(writeCommentRequestDto);
        comment.writeComment(authentcatedUser, post);
        commentRepository.save(comment);
    }
}
