package com.woopaca.knoo.exception.user;

import com.woopaca.knoo.exception.KnooException;
import lombok.Getter;

@Getter
public abstract class UserException extends KnooException {

    public UserException(UserError userError) {
        super(userError);
    }
}
