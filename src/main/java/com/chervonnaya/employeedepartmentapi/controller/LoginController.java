package com.chervonnaya.employeedepartmentapi.controller;


import com.chervonnaya.employeedepartmentapi.dto.AuthResponse;
import com.chervonnaya.employeedepartmentapi.dto.LoginDTO;
import com.chervonnaya.employeedepartmentapi.dto.UserDTO;
import com.chervonnaya.employeedepartmentapi.model.User;
import com.chervonnaya.employeedepartmentapi.security.JwtAuthenticationToken;
import com.chervonnaya.employeedepartmentapi.security.JwtUtil;
import com.chervonnaya.employeedepartmentapi.service.impl.LoginService;
import com.chervonnaya.employeedepartmentapi.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO request) throws AuthenticationException {

        return ResponseEntity.ok(loginService.login(request));
    }

}
