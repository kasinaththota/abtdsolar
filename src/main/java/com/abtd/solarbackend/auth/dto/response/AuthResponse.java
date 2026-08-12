package com.abtd.solarbackend.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private String token;

    private String username;

    private String fullName;

    private String role;

    private Long expiresIn;
}