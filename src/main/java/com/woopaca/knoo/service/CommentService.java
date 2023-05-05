package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.comment.CommentLikeResponseDto;
import com.woopaca.knoo.controller.dto.comment.WriteCommentRequestDto;
import org.springframework.lang.Nullable;

public interface CommentService {

    Long writeComment(final SignInUser signInUser, final WriteCommentRequestDto writeCommentRequestDto,
                      @Nullable final Long postId, @Nullable final Long commentId);

    void deleteComment(final SignInUser signInUser, final Long commentId);

    CommentLikeResponseDto changeLikesOnComment(final SignInUser signInUser, final Long commentId);
}
