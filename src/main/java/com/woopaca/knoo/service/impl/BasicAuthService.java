package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.controller.dto.SignUpRequestDto;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.Verification;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.AuthService;
import com.woopaca.knoo.service.MailService;
import com.woopaca.knoo.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final MailService mailService;

    @Override
    @Transactional
    public Long signUp(final SignUpRequestDto signUpRequestDto) {
        userValidator.validateSignUpUser(signUpRequestDto);

        User joinUser = User.join(signUpRequestDto);
        Verification verification = Verification.createVerification();
        joinUser.setVerification(verification);

        mailService.sendAuthMail(joinUser.getEmail(),
                joinUser.getVerification().getVerification_code());

        userRepository.save(joinUser);
        return joinUser.getId();
    }
}
