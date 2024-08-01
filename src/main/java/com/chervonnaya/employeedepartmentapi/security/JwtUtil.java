package com.chervonnaya.employeedepartmentapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${my_encrypt_key}")
    private String secretKey;
    private final long EXPIRATION_TIME = 86400000;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(getSecretKey())
            .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) throws AuthenticationException {
        Claims claims;
        try {
            claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (Exception e) {
            throw new AuthenticationException("Bad token!");
        }
        String subject = claims.getSubject();
        if (!userDetails.getUsername().equals(subject)) {
            throw new AuthenticationException("Incorrect username or password!");
        }
        if (claims.getExpiration().before(new Date())) {
            throw new AuthenticationException("Jwt is expired!");
        }
        return true;
    }

    public SecretKey getSecretKey()  {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}