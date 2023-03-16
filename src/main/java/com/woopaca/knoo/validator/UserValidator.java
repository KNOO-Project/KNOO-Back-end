package com.woopaca.knoo.validator;

import com.woopaca.knoo.controller.dto.SignUpRequestDto;

public interface UserValidator {

    void validateSignUpUser(final SignUpRequestDto signUpRequestDto);
}
