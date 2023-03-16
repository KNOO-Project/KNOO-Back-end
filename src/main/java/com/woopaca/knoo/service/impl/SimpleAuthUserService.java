package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.controller.dto.SignUpRequestDto;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.UserService;
import com.woopaca.knoo.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimpleAuthUserService implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    @Transactional
    public Long signUp(final SignUpRequestDto signUpRequestDto) {
        userValidator.validateSignUpUser(signUpRequestDto);
        User user = User.from(signUpRequestDto);
        User joinUser = userRepository.save(user);
        return joinUser.getId();
    }
}
