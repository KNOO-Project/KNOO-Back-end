package com.woopaca.knoo.exception.user.impl;

import com.woopaca.knoo.exception.KnooError;
import com.woopaca.knoo.exception.user.UserError;
import com.woopaca.knoo.exception.user.UserException;

public class InvalidEmailDomainException extends UserException {

    public InvalidEmailDomainException() {
        super(UserError.INVALID_EMAIL_DOMAIN);
    }
}
