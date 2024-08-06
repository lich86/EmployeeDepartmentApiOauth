package com.chervonnaya.employeedepartmentapi.security.oauth;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component("github")
public class GithubOAuth2UserDataHandler implements OAuth2UserDataHandler {

    @Override
    public String getEmail(OAuth2User oAuth2User) {
        String email;
        if (oAuth2User.getAttribute("email") != null) {
            email = oAuth2User.getAttribute("email");
        } else {
            email = oAuth2User.getAttribute("id") + "_" + oAuth2User.getAttribute("login");
        }
        return email;
    }

}
