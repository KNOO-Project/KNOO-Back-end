package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.dto.SignUpRequestDto;

public interface AuthService {

    Long signUp(final SignUpRequestDto signUpRequestDto);

    void mailVerify(final String code);
}
