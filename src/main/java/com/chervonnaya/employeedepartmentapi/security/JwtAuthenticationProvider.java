package com.chervonnaya.employeedepartmentapi.security;

import com.chervonnaya.employeedepartmentapi.service.impl.UserServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof JwtAuthenticationToken)) {
            return null;
        }

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        String jwtToken = (String) token.getCredentials();
        UserDetails userDetails = (UserDetails) token.getPrincipal();

        try {
            if (!jwtUtil.validateToken(jwtToken, userDetails)) {
                throw new BadCredentialsException("Invalid JWT token");
            }
        } catch (org.apache.tomcat.websocket.AuthenticationException e) {
            throw new RuntimeException(e);
        } catch (BadCredentialsException e) {
            throw new RuntimeException(e);
        }

        return new JwtAuthenticationToken(userDetails, jwtToken, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
