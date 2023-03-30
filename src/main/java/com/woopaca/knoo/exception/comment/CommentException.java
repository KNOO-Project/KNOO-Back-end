package com.woopaca.knoo.exception.comment;

import com.woopaca.knoo.exception.KnooException;
import lombok.Getter;

@Getter
public abstract class CommentException extends KnooException {

    public CommentException(CommentError commentError) {
        super(commentError);
    }
}
