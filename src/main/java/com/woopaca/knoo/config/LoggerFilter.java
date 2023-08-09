package com.woopaca.knoo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class LoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(wrappedRequest, wrappedResponse);

        StringBuilder requestHeaderValues = new StringBuilder();
        wrappedRequest.getHeaderNames().asIterator().forEachRemaining(headerKey -> {
            String headerValue = wrappedRequest.getHeader(headerKey);
            requestHeaderValues
                    .append("[")
                    .append(headerKey)
                    .append(": ")
                    .append(headerValue)
                    .append("] ");
        });

        String requestBody = new String(wrappedRequest.getContentAsByteArray());
        String requestURI = wrappedRequest.getRequestURI();
        String requestMethod = wrappedRequest.getMethod();
        log.info("\nRequest >>>>>\nURI: '{}'\nMethod: {}\nHeader: {}\nBody: {}",
                requestURI, requestMethod, requestHeaderValues, requestBody);

        StringBuilder responseHeaderValues = new StringBuilder();
        wrappedResponse.getHeaderNames().forEach(headerKey -> {
            String headerValue = wrappedResponse.getHeader(headerKey);
            responseHeaderValues
                    .append("[")
                    .append(headerKey)
                    .append(": ")
                    .append(headerValue)
                    .append("] ");
        });

        String responseBody = new String(wrappedResponse.getContentAsByteArray());
        log.info("\n<<<<< Response\nURI: '{}'\nMethod: {}\nHeader: {}\nBody: {}",
                requestURI, requestMethod, responseHeaderValues, responseBody);

        wrappedResponse.copyBodyToResponse();
    }
}
