package com.abtd.solarbackend.auth.controller;

import com.abtd.solarbackend.auth.dto.request.LoginRequest;
import com.abtd.solarbackend.auth.dto.request.RegisterRequest;
import com.abtd.solarbackend.auth.dto.response.AuthResponse;
import com.abtd.solarbackend.auth.service.AuthService;
import com.abtd.solarbackend.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse response = authService.register(request);

        return ResponseBuilder.created(
                "User registered successfully",
                response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return ResponseBuilder.ok(
                "Login successful",
                response);
    }
}