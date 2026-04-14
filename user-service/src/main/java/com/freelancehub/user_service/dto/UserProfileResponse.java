package com.freelancehub.user_service.dto;

import com.freelancehub.user_service.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private String bio;
    private String skills;
    private String portfolioUrl;
}