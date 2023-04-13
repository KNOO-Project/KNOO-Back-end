package com.woopaca.knoo.service;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.user.UserInfoResponseDto;

public interface UserService {

    UserInfoResponseDto userInfo(final SignInUser signInUser);
}
