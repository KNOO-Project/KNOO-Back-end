package com.woopaca.knoo.exception.post.impl;

import com.woopaca.knoo.exception.post.PostError;
import com.woopaca.knoo.exception.post.PostException;

public class PostCategoryNotFound extends PostException {

    public PostCategoryNotFound() {
        super(PostError.POST_CATEGORY_NOT_FOUND);
    }
}
