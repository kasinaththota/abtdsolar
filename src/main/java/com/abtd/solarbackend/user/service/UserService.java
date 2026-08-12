package com.abtd.solarbackend.user.service;

import com.abtd.solarbackend.user.dto.request.ChangePasswordRequest;
import com.abtd.solarbackend.user.dto.request.ChangeRoleRequest;
import com.abtd.solarbackend.user.dto.request.ChangeStatusRequest;
import com.abtd.solarbackend.user.dto.request.CreateUserRequest;
import com.abtd.solarbackend.user.dto.request.UpdateUserRequest;
import com.abtd.solarbackend.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserResponse> getAllUsers(Pageable pageable);

    UserResponse getUserById(Long id);

    UserResponse createUser(CreateUserRequest request);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    UserResponse changeRole(Long id, ChangeRoleRequest request);

    UserResponse changeStatus(Long id, ChangeStatusRequest request);

    void changePassword(Long id, ChangePasswordRequest request);

    void deleteUser(Long id);
}