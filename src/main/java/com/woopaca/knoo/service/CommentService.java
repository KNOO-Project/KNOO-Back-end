package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.comment.dto.WriteCommentRequestDto;
import org.springframework.lang.Nullable;

public interface CommentService {

    Long writeComment(final WriteCommentRequestDto writeCommentRequestDto,
                      @Nullable final Long postId, @Nullable final Long commentId,
                      final String authorization);

    void deleteComment(final String authorization, final Long commentId);
}
