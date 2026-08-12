package com.abtd.solarbackend.user.dto.response;

import com.abtd.solarbackend.enums.UserRole;
import com.abtd.solarbackend.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String fullName;

    private String username;

    private String email;

    private String mobile;

    private UserRole role;

    private UserStatus status;
}