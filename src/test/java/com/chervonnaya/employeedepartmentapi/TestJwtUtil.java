package com.chervonnaya.employeedepartmentapi;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TestJwtUtil {

    @Value("${my_encrypt_key}")
    private String secretKey;
    private final long EXPIRATION_TIME = 86400000;

    public String generateAdminToken() {
        return Jwts.builder()
            .subject("admin@example.com")
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(getSecretKey())
            .compact();
    }

    public String generateUserToken() {
        return Jwts.builder()
            .subject("user@example.com")
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(getSecretKey())
            .compact();
    }

    public String generateModeratorToken() {
        return Jwts.builder()
            .subject("moderator@example.com")
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(getSecretKey())
            .compact();
    }

    public SecretKey getSecretKey()  {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
