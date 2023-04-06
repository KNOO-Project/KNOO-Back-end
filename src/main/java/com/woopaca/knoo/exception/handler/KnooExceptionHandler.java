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
public class KnooExceptionHandler {

    @ExceptionHandler(KnooException.class)
    protected ResponseEntity<ErrorResponseDto> knooExceptionHandler(
            KnooException exception, HttpServletRequest request
    ) {
        log.error("errorCode: {}, path: {}, message: {}", exception.getKnooError().getErrorCode(),
                request.getRequestURI(), exception.getKnooError().getMessage());
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(exception, request);
        return ResponseEntity.status(errorResponseDto.getStatusCode())
                .body(errorResponseDto);
    }
}
