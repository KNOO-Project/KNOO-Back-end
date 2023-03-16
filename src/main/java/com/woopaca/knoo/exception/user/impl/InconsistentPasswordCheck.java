package com.woopaca.knoo.exception.user.impl;

import com.woopaca.knoo.exception.KnooError;
import com.woopaca.knoo.exception.user.UserError;
import com.woopaca.knoo.exception.user.UserException;

public class InconsistentPasswordCheck extends UserException {

    public InconsistentPasswordCheck() {
        super(UserError.INCONSISTENT_PASSWORD_CHECK);
    }
}
