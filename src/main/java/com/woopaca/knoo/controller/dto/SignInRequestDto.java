package com.woopaca.knoo.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInRequestDto {

    private String username;
    private String password;

    @Builder
    public SignInRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
