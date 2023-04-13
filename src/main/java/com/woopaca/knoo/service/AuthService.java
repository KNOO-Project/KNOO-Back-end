package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.dto.auth.SignInRequestDto;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.auth.SignUpRequestDto;
import com.woopaca.knoo.entity.User;

public interface AuthService {

    User getAuthenticatedUser(final SignInUser signInUser);

    Long signUp(final SignUpRequestDto signUpRequestDto);

    void mailVerify(final String code);

    String signIn(final SignInRequestDto signInRequestDto);
}
