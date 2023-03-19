package com.woopaca.knoo.validator;

import com.woopaca.knoo.controller.dto.SignInRequestDto;
import com.woopaca.knoo.controller.dto.SignUpRequestDto;
import com.woopaca.knoo.entity.Verification;
import org.springframework.security.core.Authentication;

public interface AuthValidator {

    void validateSignUpUser(final SignUpRequestDto signUpRequestDto);

    void validateAlreadyMailVerifiedUser(final Verification verification);

    Authentication validateSignInUser(final SignInRequestDto signInRequestDto);
}
