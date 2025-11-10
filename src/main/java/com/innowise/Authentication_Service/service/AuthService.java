package com.innowise.Authentication_Service.service;

import com.innowise.Authentication_Service.auth.dto.RegisterRequest;
import com.innowise.Authentication_Service.auth.dto.UserResponse;

public interface AuthService {
    public UserResponse registerUser(RegisterRequest request);
}
