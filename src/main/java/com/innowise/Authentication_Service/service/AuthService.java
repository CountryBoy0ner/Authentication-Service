package com.innowise.Authentication_Service.service;

import com.innowise.Authentication_Service.dto.*;

public interface AuthService {
    public UserResponse registerUser(RegisterRequest request);
    public AuthResponse login(LoginRequest request);
    public AuthResponse refreshToken(RefreshTokenRequest request);
    public ValidateTokenResponse validateToken(ValidateTokenRequest request);


    }
