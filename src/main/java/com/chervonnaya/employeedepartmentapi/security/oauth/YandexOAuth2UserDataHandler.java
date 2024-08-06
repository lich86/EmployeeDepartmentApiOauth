package com.chervonnaya.employeedepartmentapi.security.oauth;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component("yandex")
public class YandexOAuth2UserDataHandler implements OAuth2UserDataHandler{
    @Override
    public String getEmail(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("default_email");
    }
}
