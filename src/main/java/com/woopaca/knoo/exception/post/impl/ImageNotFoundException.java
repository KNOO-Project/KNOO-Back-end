package com.woopaca.knoo.exception.post.impl;

import com.woopaca.knoo.exception.post.PostError;
import com.woopaca.knoo.exception.post.PostException;

public class ImageNotFoundException extends PostException {

    public ImageNotFoundException() {
        super(PostError.IMAGE_NOT_FOUND);
    }
}
