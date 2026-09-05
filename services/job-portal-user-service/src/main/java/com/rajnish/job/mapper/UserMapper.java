package com.rajnish.job.mapper;

import com.rajnish.job.dto.response.UserResponse;
import com.rajnish.job.entity.User;

public class UserMapper {

    public static UserResponse toUserResponse(User user) {

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFullName(user.getFullName());
        userResponse.setPassword(user.getPassword());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setProfileImage(user.getProfileImage());
        userResponse.setRole(user.getRole());
        userResponse.setStatus(user.getStatus());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setLastLogin(user.getLastLogin());
        return userResponse;
    }
}
