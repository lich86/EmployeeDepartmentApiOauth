package com.chervonnaya.employeedepartmentapi.security;

import com.chervonnaya.employeedepartmentapi.service.impl.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements
    ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String email = extractEmailFromEvent(event);
        if (email != null && !email.isEmpty()) {
            loginAttemptService.loginFailed(email);
        }
    }

    private String extractEmailFromEvent(AuthenticationFailureBadCredentialsEvent event) {
        Authentication auth = event.getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof String) {
            return (String) auth.getPrincipal();
        }
        return null;
    }
}
