package com.woopaca.knoo.exception.comment.impl;

import com.woopaca.knoo.exception.comment.CommentError;
import com.woopaca.knoo.exception.comment.CommentException;

public class DeletedCommentException extends CommentException {

    public DeletedCommentException() {
        super(CommentError.DELETED_COMMENT);
    }
}
