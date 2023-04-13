package com.woopaca.knoo.controller.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInRequestDto {

    @NotNull(message = "아이디를 입력해 주세요.")
    private String username;

    @NotNull(message = "비밀번호를 입력해 주세요.")
    private String password;

    @NotNull(message = "자동 로그인 선택 항목이 null입니다. (true 또는 false)")
    @JsonProperty(value = "auto_sign_in")
    private boolean autoSignIn;

    @Builder
    public SignInRequestDto(String username, String password, boolean autoSignIn) {
        this.username = username;
        this.password = password;
        this.autoSignIn = autoSignIn;
    }
}
