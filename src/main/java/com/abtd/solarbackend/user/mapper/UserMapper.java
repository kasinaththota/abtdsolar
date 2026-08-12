package com.abtd.solarbackend.user.mapper;

import com.abtd.solarbackend.auth.dto.request.RegisterRequest;
import com.abtd.solarbackend.user.dto.request.CreateUserRequest;
import com.abtd.solarbackend.user.dto.response.UserResponse;
import com.abtd.solarbackend.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Used by Auth Module
    public User toEntity(RegisterRequest request) {

        if (request == null) {
            return null;
        }

        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .role(request.getRole())
                .build();
    }

    // Used by User Management Module
    public User toEntity(CreateUserRequest request) {

        if (request == null) {
            return null;
        }

        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .role(request.getRole())
                .build();
    }

    public UserResponse toResponse(User user) {

        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}