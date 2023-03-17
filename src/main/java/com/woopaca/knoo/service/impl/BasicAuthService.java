package com.woopaca.knoo.service.impl;

import com.woopaca.knoo.controller.dto.SignUpRequestDto;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.Verification;
import com.woopaca.knoo.exception.user.impl.VerificationNotFoundException;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.repository.VerificationRepository;
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
    private final VerificationRepository verificationRepository;

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

    @Override
    @Transactional
    public void mailVerify(final String code) {
        Verification verification = verificationRepository.findByCodeWithUser(code)
                .orElseThrow(() -> new VerificationNotFoundException());
        userValidator.validateUserMailVerification(verification);

        User user = verification.getUser();
        user.verify();
    }
}
