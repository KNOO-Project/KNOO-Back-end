package com.woopaca.knoo.validator;

import com.woopaca.knoo.controller.dto.auth.SignInRequestDto;
import com.woopaca.knoo.controller.dto.auth.SignUpRequestDto;
import com.woopaca.knoo.entity.User;
import org.springframework.security.core.Authentication;

public interface AuthValidator {

    void validateSignUpUser(final SignUpRequestDto signUpRequestDto);

    void validateAlreadyMailVerifiedUser(final User user);

    Authentication validateSignInUser(final SignInRequestDto signInRequestDto);
}
