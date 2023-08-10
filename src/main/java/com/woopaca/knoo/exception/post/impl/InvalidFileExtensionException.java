package com.woopaca.knoo.exception.post.impl;

import com.woopaca.knoo.exception.post.PostError;
import com.woopaca.knoo.exception.post.PostException;

public class InvalidFileExtensionException extends PostException {

    public InvalidFileExtensionException() {
        super(PostError.INVALID_FILE_EXTENSION);
    }
}
