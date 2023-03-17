package com.woopaca.knoo.exception.user.impl;

import com.woopaca.knoo.exception.user.UserError;
import com.woopaca.knoo.exception.user.UserException;

public class VerificationNotFoundException extends UserException {

    public VerificationNotFoundException() {
        super(UserError.VERIFICATION_NOT_FOUND);
    }
}
