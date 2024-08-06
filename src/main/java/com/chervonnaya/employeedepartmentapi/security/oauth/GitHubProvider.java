package com.chervonnaya.employeedepartmentapi.security.oauth;

import com.chervonnaya.employeedepartmentapi.service.impl.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class GitHubProvider implements AuthorizationProvider {

    private final RestTemplate restTemplate;
    private final OAuth2UserServiceImpl oAuth2UserService;

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    @Override
    public void revokeToken() {
        OAuth2AuthorizedClient authorizedClient = oAuth2UserService.getOAuth2AuthorizedClient();
        if (authorizedClient != null) {
            String accessToken = authorizedClient.getAccessToken().getTokenValue();
            String url = "https://api.github.com/applications/" + clientId + "/token";
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(clientId, clientSecret);
            headers.setContentType(MediaType.APPLICATION_JSON);
            final JSONObject body = new JSONObject();
            body.put("access_token", accessToken);
            HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

            restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
            log.info(String.format("Access token of %s was successfully revoked",
                authorizedClient.getPrincipalName()));
        }
    }
}