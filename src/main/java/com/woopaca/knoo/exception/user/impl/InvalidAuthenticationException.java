package com.woopaca.knoo.exception.user.impl;

import com.woopaca.knoo.exception.user.UserError;
import com.woopaca.knoo.exception.user.UserException;

public class InvalidAuthenticationException extends UserException {

    public InvalidAuthenticationException() {
        super(UserError.INVALID_AUTHENTICATION);
    }
}
