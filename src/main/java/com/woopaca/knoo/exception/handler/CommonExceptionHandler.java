package com.woopaca.knoo.exception.handler;

import com.woopaca.knoo.exception.handler.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> fieldInvalidExceptionHandler(
            MethodArgumentNotValidException exception, HttpServletRequest request
    ) {
        return createResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getFieldError().getDefaultMessage(), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> requestBodyNotReadableExceptionHandler(
            HttpMessageNotReadableException exception, HttpServletRequest request
    ) {
        return createResponseEntity(HttpStatus.BAD_REQUEST,
                "메시지 바디를 읽을 수 없습니다.", request);
    }

    private static ResponseEntity<ErrorResponseDto> createResponseEntity(
            HttpStatus httpStatus, String message, HttpServletRequest request
    ) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .statusCode(httpStatus.value())
                .errorType(httpStatus.name())
                .message(message)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(httpStatus)
                .body(errorResponseDto);
    }
}
