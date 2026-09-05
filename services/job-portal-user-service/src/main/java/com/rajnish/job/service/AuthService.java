package com.rajnish.job.service;

import com.rajnish.job.payload.AuthResponse;
import com.rajnish.job.payload.LoginRequest;
import com.rajnish.job.payload.SignupRequest;

public interface AuthService {

    AuthResponse signup(SignupRequest request) throws Exception;

    AuthResponse login(LoginRequest request);
}
