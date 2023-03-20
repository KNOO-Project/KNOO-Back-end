package com.woopaca.knoo.controller.user.dto;

import com.woopaca.knoo.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserInfoResponseDto {

    private String name;

    public static UserInfoResponseDto from(User user) {
        return new UserInfoResponseDto(user.getName());
    }
}
