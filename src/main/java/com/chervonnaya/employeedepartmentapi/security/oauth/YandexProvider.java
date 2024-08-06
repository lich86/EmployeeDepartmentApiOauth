package com.chervonnaya.employeedepartmentapi.security.oauth;

import com.chervonnaya.employeedepartmentapi.service.impl.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class YandexProvider implements AuthorizationProvider {

    private final RestTemplate restTemplate;
    private final OAuth2UserServiceImpl oAuth2UserService;
    @Value("${spring.security.oauth2.client.registration.yandex.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.yandex.client-secret}")
    private String clientSecret;

    @Override
    public void revokeToken() {
        OAuth2AuthorizedClient authorizedClient = oAuth2UserService.getOAuth2AuthorizedClient();
        if(authorizedClient != null) {
            String accessToken = authorizedClient.getAccessToken().getTokenValue();
            String url = "https://oauth.yandex.ru/revoke_token";
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(clientId, clientSecret);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String body = "access_token=" + accessToken + "&client_id="
                + clientId + "client_secret=" + clientSecret;

            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        }
    }
}
