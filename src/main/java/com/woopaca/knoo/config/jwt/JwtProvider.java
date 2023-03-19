package com.woopaca.knoo.config.jwt;

import com.woopaca.knoo.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private final Long HOUR_TIME = 1_000 * 60 * 60L;

    @PostConstruct
    void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(final User authenticatedUser, final int tokenExpireHour) {
        Claims claims = Jwts.claims().setSubject(authenticatedUser.getUsername());
        claims.put("id", authenticatedUser.getId());
        claims.put("roles", authenticatedUser.getRoles());

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + HOUR_TIME * tokenExpireHour))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
