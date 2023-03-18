package com.woopaca.knoo.exception.handler;

import com.woopaca.knoo.exception.handler.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> fieldInvalidExceptionHandler(
            MethodArgumentNotValidException exception, HttpServletRequest request
    ) {
        log.error("필드 검증 예외 -> {}", exception.getFieldError().getDefaultMessage());
        return createResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getFieldError().getDefaultMessage(), request, "KN201");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> requestBodyNotReadableExceptionHandler(
            HttpMessageNotReadableException exception, HttpServletRequest request
    ) {
        log.error("HTTP 메시지 바디를 읽을 수 없습니다.");
        return createResponseEntity(HttpStatus.BAD_REQUEST,
                "메시지 바디를 읽을 수 없습니다.", request, "KN202");
    }

    private static ResponseEntity<ErrorResponseDto> createResponseEntity(
            HttpStatus httpStatus, String message, HttpServletRequest request, String errorCode
    ) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .statusCode(httpStatus.value())
                .errorType(httpStatus.name())
                .message(message)
                .path(request.getRequestURI())
                .errorCode(errorCode)
                .build();
        return ResponseEntity.status(httpStatus)
                .body(errorResponseDto);
    }
}
