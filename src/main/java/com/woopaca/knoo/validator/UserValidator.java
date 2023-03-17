package com.woopaca.knoo.validator;

import com.woopaca.knoo.controller.dto.SignUpRequestDto;
import com.woopaca.knoo.entity.Verification;

public interface UserValidator {

    void validateSignUpUser(final SignUpRequestDto signUpRequestDto);

    void validateUserMailVerification(final Verification verification);
}
