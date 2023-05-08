package com.woopaca.knoo.exception.comment.impl;

import com.woopaca.knoo.exception.comment.CommentError;
import com.woopaca.knoo.exception.comment.CommentException;

public class InvalidReplyException extends CommentException {

    public InvalidReplyException() {
        super(CommentError.INVALID_REPLY);
    }
}
