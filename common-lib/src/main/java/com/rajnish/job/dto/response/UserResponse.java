package com.rajnish.job.dto.response;

import com.rajnish.job.enums.UserRole;
import com.rajnish.job.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class UserResponse {

    private Long id;
    private String fullName;
    private String password;
    private String email;
    private String phone;
    private String profileImage;
    private UserRole role;
    private UserStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}
