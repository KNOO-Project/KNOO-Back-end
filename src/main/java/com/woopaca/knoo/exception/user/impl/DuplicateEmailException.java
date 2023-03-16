package com.woopaca.knoo.exception.user.impl;

import com.woopaca.knoo.exception.user.UserError;
import com.woopaca.knoo.exception.user.UserException;

public class DuplicateEmailException extends UserException {

    public DuplicateEmailException() {
        super(UserError.DUPLICATE_EMAIL);
    }
}
