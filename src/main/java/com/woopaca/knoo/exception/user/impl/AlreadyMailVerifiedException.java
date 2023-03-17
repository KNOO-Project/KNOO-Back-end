package com.woopaca.knoo.exception.user.impl;

import com.woopaca.knoo.exception.KnooError;
import com.woopaca.knoo.exception.user.UserError;
import com.woopaca.knoo.exception.user.UserException;

public class AlreadyMailVerifiedException extends UserException {

    public AlreadyMailVerifiedException() {
        super(UserError.ALREADY_MAIL_VERIFIED);
    }
}
