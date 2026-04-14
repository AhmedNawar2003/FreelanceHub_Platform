package com.freelancehub.user_service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freelancehub.user_service.dto.AuthResponse;
import com.freelancehub.user_service.service.OAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthService oAuthService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        AuthResponse authResponse = oAuthService.handleOAuthLogin(oAuth2User);

        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String role = oAuthService.getUserRole(email);

        String redirectUrl = String.format(
                "/oauth-success.html?token=%s&name=%s&email=%s&role=%s",
                authResponse.getToken(),
                java.net.URLEncoder.encode(name, "UTF-8"),
                java.net.URLEncoder.encode(email, "UTF-8"),
                role
        );

        response.sendRedirect(redirectUrl);
    }
}