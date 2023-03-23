package com.woopaca.knoo.exception.post;

import com.woopaca.knoo.exception.KnooException;
import lombok.Getter;

@Getter
public abstract class PostException extends KnooException {

    public PostException(PostError postError) {
        super(postError);
    }
}
