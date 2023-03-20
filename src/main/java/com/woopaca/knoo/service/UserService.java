package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.user.dto.UserInfoResponseDto;

public interface UserService {

    UserInfoResponseDto userInfo(final String authorization);
}
