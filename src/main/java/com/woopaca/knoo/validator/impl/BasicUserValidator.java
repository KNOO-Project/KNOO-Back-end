package com.woopaca.knoo.validator.impl;

import com.woopaca.knoo.annotation.Validator;
import com.woopaca.knoo.controller.dto.SignUpRequestDto;
import com.woopaca.knoo.entity.EmailVerify;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.Verification;
import com.woopaca.knoo.exception.user.impl.AlreadyMailVerifiedException;
import com.woopaca.knoo.exception.user.impl.DuplicateEmailException;
import com.woopaca.knoo.exception.user.impl.DuplicateNameException;
import com.woopaca.knoo.exception.user.impl.DuplicateUsernameException;
import com.woopaca.knoo.exception.user.impl.InconsistentPasswordCheck;
import com.woopaca.knoo.exception.user.impl.InvalidEmailDomainException;
import com.woopaca.knoo.repository.UserRepository;
import com.woopaca.knoo.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Validator
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserValidator implements UserValidator {

    private final UserRepository userRepository;

    @Override
    public void validateSignUpUser(final SignUpRequestDto signUpRequestDto) {
        validateDuplicateUsername(signUpRequestDto.getUsername());
        validatePassword(signUpRequestDto.getPassword(), signUpRequestDto.getPasswordCheck());
        validateDuplicateName(signUpRequestDto.getName());
        validateEmail(signUpRequestDto.getEmail());
    }

    private void validateDuplicateUsername(final String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new DuplicateUsernameException();
        });
    }

    private void validatePassword(final String password, final String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new InconsistentPasswordCheck();
        }
    }

    private void validateDuplicateName(final String name) {
        if (userRepository.existsByName(name)) {
            throw new DuplicateNameException();
        }
    }

    private void validateEmail(final String email) {
        if (!email.endsWith("@smail.kongju.ac.kr")) {
            throw new InvalidEmailDomainException();
        }
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new DuplicateEmailException();
        });
    }

    @Override
    public void validateUserMailVerification(final Verification verification) {
        User user = verification.getUser();
        EmailVerify emailVerify = user.getEmailVerify();

        if (emailVerify.equals(EmailVerify.ENABLE)) {
            throw new AlreadyMailVerifiedException();
        }
    }
}
