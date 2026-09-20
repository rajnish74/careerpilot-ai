package com.rajnish.job.service;

import com.rajnish.job.dto.response.UserResponse;
import com.rajnish.job.entity.User;
import com.rajnish.job.payload.UpdateUserRequest;

import java.util.List;

public interface UserService {

    User getUserByEmail(String email) throws Exception;

    User getUserById(Long id) throws Exception;

    List<User> getAllUsers();

    UserResponse updateProfile(String email, UpdateUserRequest request) throws Exception;

//    Admin Action

    UserResponse suspendUser(Long id) throws Exception;

    UserResponse activateUser(Long id) throws Exception;

    UserResponse deleteUser(Long id) throws Exception;
}
