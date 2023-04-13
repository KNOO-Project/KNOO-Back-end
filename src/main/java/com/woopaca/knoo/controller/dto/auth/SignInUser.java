package com.woopaca.knoo.controller.dto.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInUser {

    private Long id;
    private String username;

    @Builder
    public SignInUser(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
