package com.rajnish.job.service.impl;

import com.rajnish.job.entity.User;
import com.rajnish.job.enums.UserRole;
import com.rajnish.job.enums.UserStatus;
import com.rajnish.job.mapper.UserMapper;
import com.rajnish.job.payload.AuthResponse;
import com.rajnish.job.payload.LoginRequest;
import com.rajnish.job.payload.SignupRequest;
import com.rajnish.job.repository.UserRepository;
import com.rajnish.job.security.CustomUserDetailsService;
import com.rajnish.job.security.JwtProvider;
import com.rajnish.job.service.AuthService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;


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
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .lastLogin(LocalDateTime.now())
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication, savedUser.getId());

        AuthResponse response = new AuthResponse();
        response.setTitle("welcome "+savedUser.getFullName());
        response.setMessage("Register successfully");
        response.setJwt(jwt);
        response.setUser(UserMapper.toUserResponse(savedUser));

        return response;
    }

    @Override
    public AuthResponse login(LoginRequest request) throws Exception {
        Authentication authentication = authenticate(request.getEmail(), request.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail());
        String jwt = jwtProvider.generateToken(authentication, user.getId());
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setTitle("welcome back -- "+user.getFullName());
        response.setMessage("Login successfully");
        response.setJwt(jwt);
        response.setUser(UserMapper.toUserResponse(user));

        return response;
    }

    private Authentication authenticate( String email, String password) throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new Exception("User not found with email "+email);
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new Exception("Wrong password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
    }
}
