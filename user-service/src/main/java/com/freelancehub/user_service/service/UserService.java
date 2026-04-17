package com.freelancehub.user_service.service;

import com.freelancehub.user_service.dto.UpdateProfileRequest;
import com.freelancehub.user_service.dto.UserProfileResponse;
import com.freelancehub.user_service.entity.User;
import com.freelancehub.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Cacheable(value = "users", key = "#id")
    public UserProfileResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    @CacheEvict(value = "users", key = "#result.id")
    public UserProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getSkills() != null) user.setSkills(request.getSkills());
        if (request.getPortfolioUrl() != null) user.setPortfolioUrl(request.getPortfolioUrl());

        userRepository.save(user);
        return mapToResponse(user);
    }

    private UserProfileResponse mapToResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .bio(user.getBio())
                .skills(user.getSkills())
                .portfolioUrl(user.getPortfolioUrl())
                .build();
    }
}