package com.woopaca.knoo.exception.handler;

import com.woopaca.knoo.exception.handler.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> fieldInvalidExceptionHandler(
            MethodArgumentNotValidException exception, HttpServletRequest request
    ) {
        log.error("필드 검증 예외 -> {}", exception.getFieldError().getDefaultMessage());
        return createResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getFieldError().getDefaultMessage(), request, "KN101");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponseDto> requestBodyNotReadableExceptionHandler(
            HttpServletRequest request
    ) {
        log.error("HTTP 메시지 바디를 읽을 수 없습니다.");
        return createResponseEntity(HttpStatus.BAD_REQUEST,
                "메시지 바디를 읽을 수 없습니다.", request, "KN102");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponseDto> httpMethodNotSupportedExceptionHandler(
            HttpServletRequest request
    ) {
        log.error("지원하지 않는 HTTP Method");
        return createResponseEntity(HttpStatus.BAD_REQUEST,
                "지원하지 않는 HTTP Method 입니다.", request, "KN103");
    }

    @ExceptionHandler(MissingPathVariableException.class)
    protected ResponseEntity<ErrorResponseDto> pathVariableExceptionHandler(
            HttpServletRequest request
    ) {
        log.error("경로 변수 값이 존재하지 않거나 올바르지 않습니다.");
        return createResponseEntity(HttpStatus.BAD_REQUEST,
                "경로 변수 값이 올바르지 않습니다.", request, "KN104");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponseDto> requestParameterExceptionHandler(
            HttpServletRequest request
    ) {
        log.error("요청 파라미터가 누락되었습니다.");
        return createResponseEntity(HttpStatus.BAD_REQUEST,
                "요청 파라미터가 누락되었습니다.", request, "KN105");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponseDto> typeMismatchExceptionHandler(
            HttpServletRequest request
    ) {
        log.error("요청 파라미터 형식이 올바르지 않습니다.");
        return createResponseEntity(HttpStatus.BAD_REQUEST,
                "요청 파라미터 형식이 올바르지 않습니다.", request, "KN106");
    }

    private ResponseEntity<ErrorResponseDto> createResponseEntity(
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
