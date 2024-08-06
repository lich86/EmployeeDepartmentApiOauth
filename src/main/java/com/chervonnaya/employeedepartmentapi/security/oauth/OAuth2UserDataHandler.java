package com.chervonnaya.employeedepartmentapi.security.oauth;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserDataHandler {

    String getEmail(OAuth2User oAuth2User);
}
