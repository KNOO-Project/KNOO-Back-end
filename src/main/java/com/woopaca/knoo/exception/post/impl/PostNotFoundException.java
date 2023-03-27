package com.woopaca.knoo.exception.post.impl;

import com.woopaca.knoo.exception.post.PostError;
import com.woopaca.knoo.exception.post.PostException;

public class PostNotFoundException extends PostException {

    public PostNotFoundException() {
        super(PostError.POST_NOT_FOUND);
    }
}
