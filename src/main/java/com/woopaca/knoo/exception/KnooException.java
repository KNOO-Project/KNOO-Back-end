package com.woopaca.knoo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class KnooException extends RuntimeException {

    private KnooError knooError;
}
