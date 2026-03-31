package com.aditi_final.bridgeskill_api.service;

import com.aditi_final.bridgeskill_api.dto.auth.*;
import com.aditi_final.bridgeskill_api.entity.User;
import com.aditi_final.bridgeskill_api.exception.DuplicateResourceException;
import com.aditi_final.bridgeskill_api.exception.ResourceNotFoundException;
import com.aditi_final.bridgeskill_api.repository.UserRepository;
import com.aditi_final.bridgeskill_api.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        if (request.getRoleId() == null || (!request.getRoleId().equals(2L) && !request.getRoleId().equals(3L))) {
            throw new IllegalArgumentException("Invalid role selected");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password do not match");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleId(request.getRoleId())
                .build();

        userRepository.save(user);

        return new RegisterResponse("User registered successfully");
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                user.getFullName(),
                user.getEmail(),
                user.getRoleId()
        );
    }

    public MeResponse me(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new MeResponse(
                user.getFullName(),
                user.getEmail(),
                user.getRoleId()
        );
    }
}