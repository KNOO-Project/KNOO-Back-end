package com.woopaca.knoo.controller.auth;

import com.woopaca.knoo.annotation.SignIn;
import com.woopaca.knoo.config.jwt.JwtUtils;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RequiredArgsConstructor
public class SignInUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtils jwtUtils;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SignIn.class) &&
                SignInUser.class.equals(parameter.getParameterType());
    }

    @Override
    public SignInUser resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = AuthorizationExtractor
                .extract(Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)));
        return jwtUtils.getSignInUserByToken(token);
    }
}
