package com.woopaca.knoo.exception.user;

import com.woopaca.knoo.exception.KnooError;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum UserError implements KnooError {

    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "중복된 회원 아이디입니다.", "KN001"),
    INCONSISTENT_PASSWORD_CHECK(HttpStatus.BAD_REQUEST, "비밀번호 확인이 일치하지 않습니다.", "KN002"),
    DUPLICATE_NAME(HttpStatus.CONFLICT, "중복된 회원 이름입니다.", "KN003"),
    INVALID_EMAIL_DOMAIN(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 도메인입니다. [smail.kongju.ac.kr]", "KN004"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 회원 이메일입니다.", "KN005"),
    VERIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "진행 중인 이메일 인증을 찾을 수 없습니다.", "KN006"),
    ALREADY_MAIL_VERIFIED(HttpStatus.BAD_REQUEST, "이미 이메일 인증이 완료되었습니다.", "KN007");

    private final HttpStatus httpStatus;
    private final String message;
    private final String errorCode;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
