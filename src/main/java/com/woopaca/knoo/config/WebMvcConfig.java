package com.woopaca.knoo.config;

import com.woopaca.knoo.config.converter.PostCategoryConverter;
import com.woopaca.knoo.config.converter.SearchConditionConverter;
import com.woopaca.knoo.config.jwt.JwtUtils;
import com.woopaca.knoo.controller.auth.SignInUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtUtils jwtUtils;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new PostCategoryConverter());
        registry.addConverter(new SearchConditionConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SignInUserArgumentResolver(jwtUtils));
    }
}
