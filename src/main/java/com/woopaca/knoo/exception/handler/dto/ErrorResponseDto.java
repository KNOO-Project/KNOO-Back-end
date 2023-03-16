package com.woopaca.knoo.exception.handler.dto;

import com.woopaca.knoo.exception.KnooException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponseDto {

    private int statusCode;
    private String errorType;
    private String message;
    private String path;

    @Builder
    public ErrorResponseDto(int statusCode, String errorType, String message, String path) {
        this.statusCode = statusCode;
        this.errorType = errorType;
        this.message = message;
        this.path = path;
    }

    public static ErrorResponseDto of(KnooException exception, HttpServletRequest request) {
        return ErrorResponseDto.builder()
                .statusCode(exception.getKnooError().getHttpStatus().value())
                .errorType(exception.getKnooError().getHttpStatus().name())
                .message(exception.getKnooError().getMessage())
                .path(request.getRequestURI())
                .build();
    }
}
