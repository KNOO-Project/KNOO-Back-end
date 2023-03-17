package com.woopaca.knoo.exception.mail;

import com.woopaca.knoo.exception.KnooError;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum MailError implements KnooError {

    MESSAGE_CAN_NOT_CREATE(HttpStatus.INTERNAL_SERVER_ERROR, "메일 메시지를 생성할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
