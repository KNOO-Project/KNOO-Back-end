package com.woopaca.knoo.controller.user;

import com.woopaca.knoo.controller.user.dto.UserInfoResponseDto;
import com.woopaca.knoo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserInfoResponseDto> userProfile(
            @RequestHeader("Authorization") String authorization) {
        UserInfoResponseDto userInfoResponseDto = userService.userInfo(authorization);
        return ResponseEntity.ok().body(userInfoResponseDto);
    }
}
