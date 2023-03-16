package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.dto.SignUpRequestDto;

public interface UserService {

    Long signUp(final SignUpRequestDto signUpRequestDto);
}
