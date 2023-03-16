package com.woopaca.knoo.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SignUpRequestDto {

    @NotBlank(message = "회원 아이디는 비어있을 수 없습니다.")
    @Size(min = 4, max = 20, message = "회원 아이디는 4자 이상, 20자 이하이어야 합니다.")
    private String username;
    @NotBlank(message = "회원 비밀번호는 비어있을 수 없습니다.")
    @Size(min = 6, max = 20, message = "회원 비밀번호는 6자 이상, 20자 이하이어야 합니다.")
    private String password;
    @NotBlank(message = "비밀번호 확인은 비어있을 수 없습니다.")
    private String passwordCheck;
    @NotBlank(message = "회원 이름은 비어있을 수 없습니다.")
    @Size(min = 2, max = 10, message = "회원 이름은 2자 이상, 10자 이하이어야 합니다.")
    private String name;
    @NotBlank(message = "회원 이메일은 비어있을 수 없습니다.")
    @Email(message = "회원 이메일 형식이 올바르지 않습니다.")
    private String email;
}
