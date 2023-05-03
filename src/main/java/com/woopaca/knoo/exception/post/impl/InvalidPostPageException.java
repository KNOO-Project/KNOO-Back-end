package com.woopaca.knoo.exception.post.impl;

import com.woopaca.knoo.exception.post.PostError;
import com.woopaca.knoo.exception.post.PostException;

public class InvalidPostPageException extends PostException {

    public InvalidPostPageException() {
        super(PostError.INVALID_POST_PAGE);
    }
}
