package com.woopaca.knoo.exception.comment.impl;

import com.woopaca.knoo.exception.comment.CommentError;
import com.woopaca.knoo.exception.comment.CommentException;

public class CommentNotFoundException extends CommentException {

    public CommentNotFoundException() {
        super(CommentError.COMMENT_NOT_FOUND);
    }
}
