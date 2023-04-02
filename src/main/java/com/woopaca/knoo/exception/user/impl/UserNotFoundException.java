package com.woopaca.knoo.exception.user.impl;

import com.woopaca.knoo.exception.user.UserError;
import com.woopaca.knoo.exception.user.UserException;

public class UserNotFoundException extends UserException {

    public UserNotFoundException() {
        super(UserError.USER_NOT_FOUND);
    }
}
