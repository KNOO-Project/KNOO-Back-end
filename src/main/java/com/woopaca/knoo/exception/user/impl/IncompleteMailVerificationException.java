package com.woopaca.knoo.exception.user.impl;

import com.woopaca.knoo.exception.user.UserError;
import com.woopaca.knoo.exception.user.UserException;

public class IncompleteMailVerificationException extends UserException {

    public IncompleteMailVerificationException() {
        super(UserError.INCOMPLETE_MAIL_VERIFICATION);
    }
}
