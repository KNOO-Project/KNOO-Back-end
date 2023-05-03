package com.woopaca.knoo.exception.post.impl;

import com.woopaca.knoo.exception.post.PostError;
import com.woopaca.knoo.exception.post.PostException;

public class PageCountExceededException extends PostException {

    public PageCountExceededException() {
        super(PostError.OUT_OF_PAGE);
    }
}
