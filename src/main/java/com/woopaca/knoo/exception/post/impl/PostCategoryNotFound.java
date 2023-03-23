package com.woopaca.knoo.exception.post.impl;

import com.woopaca.knoo.exception.post.PostError;
import com.woopaca.knoo.exception.post.PostException;

public class PostCategoryNotFound extends PostException {

    public PostCategoryNotFound(PostError postError) {
        super(postError);
    }
}
