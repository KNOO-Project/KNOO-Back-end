package com.woopaca.knoo.controller;

import com.woopaca.knoo.controller.dto.SignUpRequestDto;
import com.woopaca.knoo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> userSignUp(@RequestBody @Valid final SignUpRequestDto signUpRequestDto) {
        log.info("userSignUp");
        Long joinUserId = authService.signUp(signUpRequestDto);
        return ResponseEntity.created(URI.create("/users/" + joinUserId))
                .body("회원가입 인증 이메일이 전송되었습니다.");
    }
}
