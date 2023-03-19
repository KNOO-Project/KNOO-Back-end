package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.config.jwt.JwtProvider;
import com.woopaca.knoo.controller.dto.SignInRequestDto;
import com.woopaca.knoo.controller.dto.SignUpRequestDto;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.Verification;
import com.woopaca.knoo.exception.user.impl.InvalidAuthenticationException;
import com.woopaca.knoo.exception.user.impl.VerificationNotFoundException;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.repository.VerificationRepository;
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

    private final UserRepository userRepository;
    private final AuthValidator authValidator;
    private final MailService mailService;
    private final VerificationRepository verificationRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Long signUp(final SignUpRequestDto signUpRequestDto) {
        authValidator.validateSignUpUser(signUpRequestDto);

        User joinUser = User.join(signUpRequestDto);
        joinUser.encodePassword(passwordEncoder);
        Verification verification = Verification.createVerification();
        joinUser.setVerification(verification);

        userRepository.save(joinUser);

        mailService.sendAuthMail(joinUser.getEmail(),
                joinUser.getVerification().getVerification_code());
        return joinUser.getId();
    }

    @Override
    @Transactional
    public void mailVerify(final String code) {
        Verification verification = verificationRepository.findByCodeWithUser(code)
                .orElseThrow(() -> new VerificationNotFoundException());
        authValidator.validateAlreadyMailVerifiedUser(verification);

        User user = verification.getUser();
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
            return "Bearer " + jwtProvider.createToken(authenticatedUser, 24 * 7);
        }
        return "Bearer " + jwtProvider.createToken(authenticatedUser, 3);
    }
}
