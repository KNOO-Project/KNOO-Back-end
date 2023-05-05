package com.woopaca.knoo.exception.comment;

import com.woopaca.knoo.exception.KnooError;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum CommentError implements KnooError {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다.", "KN401"),
    DELETED_COMMENT(HttpStatus.BAD_REQUEST, "삭제된 댓글입니다.", "KN402");

    private final HttpStatus httpStatus;
    private final String message;
    private final String errorCode;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
