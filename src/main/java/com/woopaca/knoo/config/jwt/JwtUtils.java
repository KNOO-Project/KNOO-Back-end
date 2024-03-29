package com.woopaca.knoo.config.jwt;

import com.woopaca.knoo.controller.dto.auth.SignInUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;
    private final UserDetailsService userDetailsService;

    @PostConstruct
    void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String resolveToken(String authorization) {
        if (authorization == null) {
            return null;
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException();
        }

        return authorization.split(" ")[1];
    }

    public boolean isNotExpired(String token) {
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);

        return !claims.getBody()
                .getExpiration()
                .before(new Date());
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsernameByToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    public String getUsernameByToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public SignInUser getSignInUserByToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        Long id = Long.parseLong(claims.get("id").toString());
        String username = claims.getSubject();

        return SignInUser.builder()
                .id(id)
                .username(username)
                .build();
    }
}
