package com.chervonnaya.employeedepartmentapi.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;


@Service
public class LoginAttemptService {

    @Value("${loginAttempt.max}")
    public int MAX_ATTEMPT;

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    private HttpServletRequest request;

    private static final String LOGIN_ATTEMPTS_PREFIX = "loginAttempts";

    public void loginFailed(final String key) {
        String redisKey = LOGIN_ATTEMPTS_PREFIX + key;
        Integer attempts = redisTemplate.opsForValue().get(redisKey);
        if (attempts == null) {
            attempts = 0;
        }
        attempts++;
        redisTemplate.opsForValue().set(redisKey, attempts, 1, TimeUnit.DAYS);
    }

    public boolean isBlocked() {
        String redisKey = LOGIN_ATTEMPTS_PREFIX + getClientIP();
        Integer attempts = redisTemplate.opsForValue().get(redisKey);
        return attempts != null && attempts >= MAX_ATTEMPT;
    }

    private String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
