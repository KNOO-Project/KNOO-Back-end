package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.dto.auth.SignInRequestDto;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.auth.SignUpRequestDto;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.exception.user.impl.InvalidAuthenticationException;
import com.woopaca.knoo.exception.user.impl.UserNotFoundException;
import com.woopaca.knoo.exception.user.impl.VerificationNotFoundException;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.service.AuthService;
import com.woopaca.knoo.service.MailService;
import com.woopaca.knoo.validator.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicAuthService implements AuthService {

    public static final int HOURS_OF_DAY = 24;
    private final UserRepository userRepository;
    private final AuthValidator authValidator;
    private final MailService mailService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getAuthenticatedUser(final SignInUser signInUser) {
        return userRepository.findById(signInUser.getId()).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public Long signUp(final SignUpRequestDto signUpRequestDto) {
        authValidator.validateSignUpUser(signUpRequestDto);
        User joinUser = getJoinUser(signUpRequestDto);

        mailService.sendAuthMail(joinUser.getEmail(), joinUser.getVerificationCode());
        return joinUser.getId();
    }

    private User getJoinUser(SignUpRequestDto signUpRequestDto) {
        User joinUser = User.join(signUpRequestDto);
        joinUser.encodePassword(passwordEncoder);
        userRepository.save(joinUser);
        return joinUser;
    }

    @Transactional
    @Override
    public void mailVerify(final String code) {
        User user = userRepository.findByVerificationCode(code).orElseThrow(VerificationNotFoundException::new);
        authValidator.validateAlreadyMailVerifiedUser(user);
        user.verify();
    }

    @Override
    public String signIn(final SignInRequestDto signInRequestDto) {
        Authentication authentication = authValidator.validateSignInUser(signInRequestDto);
        if (!authentication.isAuthenticated()) {
            throw new InvalidAuthenticationException();
        }

        User authenticatedUser = (User) authentication.getPrincipal();
        if (signInRequestDto.isAutoSignIn()) {
            return "Bearer " + jwtProvider.createToken(authenticatedUser, HOURS_OF_DAY);
        }
        return "Bearer " + jwtProvider.createToken(authenticatedUser, HOURS_OF_DAY * 30);
    }
}
