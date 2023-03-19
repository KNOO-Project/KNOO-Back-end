package com.woopaca.knoo.controller;

import com.woopaca.knoo.controller.dto.SignInRequestDto;
import com.woopaca.knoo.controller.dto.SignUpRequestDto;
import com.woopaca.knoo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> userSignUp(@RequestBody @Valid final SignUpRequestDto signUpRequestDto) {
        Long joinUserId = authService.signUp(signUpRequestDto);
        return ResponseEntity.created(URI.create("/users/" + joinUserId))
                .body("회원가입 인증 이메일이 전송되었습니다.");
    }

    @GetMapping("/mail")
    public ResponseEntity<String> userMailVerify(@RequestParam("code") final String code,
                                                 final HttpServletResponse response) throws IOException {
        authService.mailVerify(code);
        response.sendRedirect("http://localhost:8888/mail-verify");
        return ResponseEntity.ok().body("이메일 인증이 완료되었습니다.");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> userSignIn(@RequestBody final SignInRequestDto signInRequestDto) {
        String token = authService.signIn(signInRequestDto);
        return ResponseEntity.ok().body(token);
    }
}
