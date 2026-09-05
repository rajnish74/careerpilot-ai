package com.rajnish.job.service.impl;

import com.rajnish.job.entity.User;
import com.rajnish.job.enums.UserRole;
import com.rajnish.job.enums.UserStatus;
import com.rajnish.job.mapper.UserMapper;
import com.rajnish.job.payload.AuthResponse;
import com.rajnish.job.payload.LoginRequest;
import com.rajnish.job.payload.SignupRequest;
import com.rajnish.job.repository.UserRepository;
import com.rajnish.job.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;


    @Override
    public AuthResponse signup(SignupRequest request) throws Exception {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exception("Email is already exists: "+request.getEmail());
        }

        if (request.getRole() == UserRole.ROLE_ADMIN) {
            throw new Exception("Admin role is not allowed for self-registration");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .lastLogin(LocalDateTime.now())
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setTitle("welcome "+savedUser.getFullName());
        response.setMessage("Register successfully");
        response.setJwt("dummy jwt");
        response.setUser(UserMapper.toUserResponse(savedUser));

        return response;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
