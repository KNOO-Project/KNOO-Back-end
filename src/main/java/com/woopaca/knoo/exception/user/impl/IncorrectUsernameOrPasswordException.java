package com.woopaca.knoo.exception.user.impl;

import com.woopaca.knoo.exception.user.UserError;
import com.woopaca.knoo.exception.user.UserException;

public class IncorrectUsernameOrPasswordException extends UserException {

    public IncorrectUsernameOrPasswordException() {
        super(UserError.INCORRECT_USERNAME_OR_PASSWORD);
    }
}
