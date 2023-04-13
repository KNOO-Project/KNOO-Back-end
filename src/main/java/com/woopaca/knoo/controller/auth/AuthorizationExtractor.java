package com.woopaca.knoo.controller.auth;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

public class AuthorizationExtractor {

    public static String extract(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authorization.split(" ")[1];
    }
}
