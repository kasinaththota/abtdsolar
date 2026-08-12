package com.abtd.solarbackend.auth.service;

import com.abtd.solarbackend.auth.dto.request.LoginRequest;
import com.abtd.solarbackend.auth.dto.request.RegisterRequest;
import com.abtd.solarbackend.auth.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}