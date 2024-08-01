package com.chervonnaya.employeedepartmentapi.service.impl;

import com.chervonnaya.employeedepartmentapi.dto.AuthResponse;
import com.chervonnaya.employeedepartmentapi.dto.LoginDTO;
import com.chervonnaya.employeedepartmentapi.security.JwtAuthenticationToken;
import com.chervonnaya.employeedepartmentapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginDTO request) {
        String password = request.getPassword();
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials");
        }
        UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        JwtAuthenticationToken jwtAuthenticationToken =
            new JwtAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
        return new AuthResponse(token);
    }
}
