package com.freelancehub.user_service.service;

import com.freelancehub.user_service.dto.AuthResponse;
import com.freelancehub.user_service.entity.User;
import com.freelancehub.user_service.enums.Role;
import com.freelancehub.user_service.repository.UserRepository;
import com.freelancehub.user_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponse handleOAuthLogin(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .fullName(name)
                            .email(email)
                            .password(UUID.randomUUID().toString())
                            .role(Role.FREELANCER)
                            .build();
                    return userRepository.save(newUser);
                });

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );
        return AuthResponse.builder().token(token).build();
    }
    public String getUserRole(String email) {
        return userRepository.findByEmail(email)
                .map(user -> user.getRole().name())
                .orElse("FREELANCER");
    }
}