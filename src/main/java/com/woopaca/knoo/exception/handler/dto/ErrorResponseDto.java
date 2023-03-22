package com.woopaca.knoo.exception.handler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.knoo.exception.KnooException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponseDto {

    @JsonProperty(value = "status_code")
    private int statusCode;
    @JsonProperty(value = "error_type")
    private String errorType;
    private String message;
    private String path;
    @JsonProperty(value = "error_code")
    private String errorCode;

    @Builder
    public ErrorResponseDto(int statusCode, String errorType, String message, String path, String errorCode) {
        this.statusCode = statusCode;
        this.errorType = errorType;
        this.message = message;
        this.path = path;
        this.errorCode = errorCode;
    }

    public static ErrorResponseDto of(KnooException exception, HttpServletRequest request) {
        return ErrorResponseDto.builder()
                .statusCode(exception.getKnooError().getHttpStatus().value())
                .errorType(exception.getKnooError().getHttpStatus().name())
                .message(exception.getKnooError().getMessage())
                .path(request.getRequestURI())
                .errorCode(exception.getKnooError().getErrorCode())
                .build();
    }
}
