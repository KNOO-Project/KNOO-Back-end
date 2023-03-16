package com.woopaca.knoo.exception.user.impl;

import com.woopaca.knoo.exception.user.UserError;
import com.woopaca.knoo.exception.user.UserException;

public class DuplicateUsernameException extends UserException {

    public DuplicateUsernameException() {
        super(UserError.DUPLICATE_USERNAME);
    }
}
