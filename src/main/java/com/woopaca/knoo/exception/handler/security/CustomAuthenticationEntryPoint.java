package com.woopaca.knoo.exception.handler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.knoo.exception.handler.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error("유효하지 않은 토큰입니다.");
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .errorType(HttpStatus.UNAUTHORIZED.name())
                .message("유효하지 않은 토큰입니다.")
                .path(request.getRequestURI())
                .errorCode("KN301")
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        response.getWriter().write(mapper.writeValueAsString(errorResponseDto));
    }
}
