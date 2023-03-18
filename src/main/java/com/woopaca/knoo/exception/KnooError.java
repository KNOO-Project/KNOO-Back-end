package com.woopaca.knoo.exception;

import org.springframework.http.HttpStatus;

public interface KnooError {

    HttpStatus getHttpStatus();

    String getMessage();

    String getErrorCode();
}
