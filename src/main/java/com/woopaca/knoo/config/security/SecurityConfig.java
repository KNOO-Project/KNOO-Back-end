package com.woopaca.knoo.config.security;

import com.woopaca.knoo.config.jwt.JwtAuthenticationFilter;
import com.woopaca.knoo.exception.handler.security.CustomAccessDeniedHandler;
import com.woopaca.knoo.exception.handler.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable() // UI 인증이 아닌 토큰 인이기 때문에 basic disable
                .csrf().disable() // Cross-Site Request Forgery 방지
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt 이용 시 사용
                .and()
                .authorizeRequests()
                // permitAll() -> 모두 허용     authenticated() -> 인증 필요     hasRole() -> 권한 필요
//                .antMatchers("/api/v1/auth/sign-up", "/api/v1/auth/sign-in").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/v1/posts").permitAll()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
