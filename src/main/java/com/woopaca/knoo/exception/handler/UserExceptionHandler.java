package com.woopaca.knoo.exception.handler;

import com.woopaca.knoo.exception.KnooException;
import com.woopaca.knoo.exception.handler.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class UserExceptionHandler {

    @ExceptionHandler(KnooException.class)
    public ResponseEntity<ErrorResponseDto> userExceptionHandler(
            KnooException exception, HttpServletRequest request
    ) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(exception, request);
        return ResponseEntity.status(errorResponseDto.getStatusCode())
                .body(errorResponseDto);
    }
}
