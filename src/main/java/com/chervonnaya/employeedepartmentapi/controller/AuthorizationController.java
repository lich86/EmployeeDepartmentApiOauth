package com.chervonnaya.employeedepartmentapi.controller;

import com.chervonnaya.employeedepartmentapi.security.oauth.GitHubProvider;
import com.chervonnaya.employeedepartmentapi.security.oauth.YandexProvider;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthorizationController {

    private GitHubProvider gitHubProvider;
    private YandexProvider yandexProvider;

    @GetMapping("/revoke")
    public String revokeToken(@RequestParam String provider) {
        try {
            switch (provider.toLowerCase()) {
                case "github":
                    gitHubProvider.revokeToken();
                    break;
                case "yandex":
                    yandexProvider.revokeToken();
                    break;
                default:
                    return "Unknown provider";
            }
            return "Token revoked successfully";
        } catch (Exception e) {
            return "Failed to revoke token: " + e.getMessage();
        }
    }
}

