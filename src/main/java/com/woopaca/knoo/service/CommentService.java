package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.comment.dto.WriteCommentRequestDto;

public interface CommentService {

    void writeComment(final WriteCommentRequestDto writeCommentRequestDto,
                      final Long postId, final String authorization);
}
