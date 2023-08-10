package com.woopaca.knoo.exception.post.impl;

import com.woopaca.knoo.exception.post.PostError;
import com.woopaca.knoo.exception.post.PostException;

public class ImageNotReadable extends PostException {

    public ImageNotReadable() {
        super(PostError.IMAGE_CAN_NOT_READ);
    }
}
