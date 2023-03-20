package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.config.jwt.JwtUtils;
import com.woopaca.knoo.controller.user.dto.UserInfoResponseDto;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.exception.user.impl.UserNotFoundException;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    public UserInfoResponseDto userInfo(final String authorization) {
        String token = jwtUtils.resolveToken(authorization);
        String username = jwtUtils.getUsernameInToken(token);

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException());
        return UserInfoResponseDto.from(user);
    }
}
