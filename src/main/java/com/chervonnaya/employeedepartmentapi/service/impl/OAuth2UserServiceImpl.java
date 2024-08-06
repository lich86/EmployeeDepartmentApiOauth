package com.chervonnaya.employeedepartmentapi.service.impl;

import com.chervonnaya.employeedepartmentapi.model.User;
import com.chervonnaya.employeedepartmentapi.model.enums.Role;
import com.chervonnaya.employeedepartmentapi.repository.UserRepository;
import com.chervonnaya.employeedepartmentapi.security.oauth.OAuth2UserDataHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {


    private final UserRepository repository;
    private final LoginAttemptService loginAttemptService;
    private final Map<String, OAuth2UserDataHandler> oAuth2UserDataHandlers;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2AuthorizedClientService authorizedClientService;



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return getOauth2User(user, userRequest);
    }

    private OAuth2User getOauth2User(OAuth2User oauth2User, OAuth2UserRequest userRequest) {
        String clientRegistrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserDataHandler dataHandler = oAuth2UserDataHandlers.get(clientRegistrationId);
        String email = dataHandler.getEmail(oauth2User);
        if (loginAttemptService.isBlocked(email)) {
            throw new RuntimeException("blocked");
        }
        Optional<User> user = repository.findByEmailIgnoreCase(email);
        DefaultOAuth2User oAuth2User;
        if (user.isPresent()) {
            Set<GrantedAuthority> auths = user.get().getRoles()
                .stream().map(r -> (GrantedAuthority) r).collect(Collectors.toSet());
            oAuth2User = new DefaultOAuth2User(auths, oauth2User.getAttributes(), "id");
            log.info(String.format("oAuth2User %s was successfully authenticated", email));
            return oAuth2User;
        } else {
            oAuth2User = (DefaultOAuth2User) saveNewUser(oauth2User, email);
            log.info(String.format("oAuth2User %s was created and successfully authenticated", email));
            return oAuth2User;
        }
    }

    private OAuth2User saveNewUser(OAuth2User user, String email) {
        repository.save(new User(email, generatePassword(), Role.ROLE_USER));
        Set<GrantedAuthority> auths = new HashSet<>(Arrays.asList(Role.ROLE_USER));
        return new DefaultOAuth2User(auths, user.getAttributes(), "id");
    }

    private String generatePassword() {
        String randomPassword = "random_password";
/*        String characters = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        String randomPassword =
            RandomStringUtils.random(6, 0, 0, true, true, characters.toCharArray());*/
        return passwordEncoder.encode(randomPassword);
    }

    public OAuth2AuthorizedClient getOAuth2AuthorizedClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2AuthToken = (OAuth2AuthenticationToken) authentication;
            String clientRegistrationId = oauth2AuthToken.getAuthorizedClientRegistrationId();
            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                clientRegistrationId, oauth2AuthToken.getName());
            if (authorizedClient != null) {
                return authorizedClient;
            }
        }
        return null;
    }

}
