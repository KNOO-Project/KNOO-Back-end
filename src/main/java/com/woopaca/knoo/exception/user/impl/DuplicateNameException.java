package com.woopaca.knoo.exception.user.impl;

import com.woopaca.knoo.exception.user.UserError;
import com.woopaca.knoo.exception.user.UserException;

public class DuplicateNameException extends UserException {

    public DuplicateNameException() {
        super(UserError.DUPLICATE_NAME);
    }
}
