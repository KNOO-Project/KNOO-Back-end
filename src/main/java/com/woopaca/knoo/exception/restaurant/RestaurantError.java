package com.woopaca.knoo.exception.restaurant;

import com.woopaca.knoo.exception.KnooError;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum RestaurantError implements KnooError {
    CAMPUS_NAME_MATCH_ERROR(HttpStatus.BAD_REQUEST, "캠퍼스 이름이 올바르지 않습니다.", "KN701");

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
