package com.woopaca.knoo.exception.notification;

import com.woopaca.knoo.exception.KnooError;
import org.springframework.http.HttpStatus;

public enum NotificationError implements KnooError {

    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다.", "KN601");

    private final HttpStatus httpStatus;
    private final String message;
    private final String errorCode;

    NotificationError(HttpStatus httpStatus, String message, String errorCode) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.errorCode = errorCode;
    }

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
