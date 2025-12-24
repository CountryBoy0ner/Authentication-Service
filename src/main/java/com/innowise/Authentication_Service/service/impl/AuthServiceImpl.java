package com.innowise.Authentication_Service.service.impl;

import com.innowise.Authentication_Service.auth.JwtService;
import com.innowise.Authentication_Service.dto.*;
import com.innowise.Authentication_Service.model.Role;
import com.innowise.Authentication_Service.model.User;
import com.innowise.Authentication_Service.repository.RoleRepository;
import com.innowise.Authentication_Service.repository.UserRepository;
import com.innowise.Authentication_Service.service.AuthService;
import io.jsonwebtoken.Claims;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        Role userRole = roleRepository
                .findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_USER");
                    return roleRepository.save(role);
                });

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .enabled(true)
                .roles(Set.of(userRole))
                .build();

        User saved = userRepository.save(user);
        UserResponse response = UserResponse.builder()
                .id(saved.getId())
                .username(saved.getUsername())
                .enabled(saved.isEnabled())
                .build();
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!user.isEnabled()) {
            throw new BadCredentialsException("User is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        Claims claims = jwtService.parseToken(refreshToken);
        String username = claims.getSubject();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public ValidateTokenResponse validateToken(ValidateTokenRequest request) {

        String token = request.getToken();

        if (!jwtService.isTokenValid(token)) {
            return ValidateTokenResponse.builder()
                    .valid(false)
                    .build();
        }

        Claims claims = jwtService.parseToken(token);

        String username = claims.getSubject();
        Long userId = claims.get("userId", Long.class);
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles");

        return ValidateTokenResponse.builder()
                .valid(true)
                .username(username)
                .userId(userId)
                .roles(roles)
                .build();
    }


}
