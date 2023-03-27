package com.woopaca.knoo.exception.post.impl;

import com.woopaca.knoo.exception.post.PostError;
import com.woopaca.knoo.exception.post.PostException;

public class PostCategoryNotFoundException extends PostException {

    public PostCategoryNotFoundException() {
        super(PostError.POST_CATEGORY_NOT_FOUND);
    }
}
