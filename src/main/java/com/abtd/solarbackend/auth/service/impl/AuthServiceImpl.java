package com.abtd.solarbackend.auth.service.impl;

import com.abtd.solarbackend.auth.dto.request.LoginRequest;
import com.abtd.solarbackend.auth.dto.request.RegisterRequest;
import com.abtd.solarbackend.auth.dto.response.AuthResponse;
import com.abtd.solarbackend.auth.security.JwtService;
import com.abtd.solarbackend.auth.service.AuthService;
import com.abtd.solarbackend.enums.UserStatus;
import com.abtd.solarbackend.user.entity.User;
import com.abtd.solarbackend.user.mapper.UserMapper;
import com.abtd.solarbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .role(user.getRole().name())
                .expiresIn(86400000L)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .role(user.getRole().name())
                .expiresIn(86400000L)
                .build();
    }
}