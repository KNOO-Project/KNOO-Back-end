package com.woopaca.knoo.exception.user.impl;

import com.woopaca.knoo.exception.user.UserError;
import com.woopaca.knoo.exception.user.UserException;

public class InvalidUserException extends UserException {

    public InvalidUserException() {
        super(UserError.INVALID_USER);
    }
}
