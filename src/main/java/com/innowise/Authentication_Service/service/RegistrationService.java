package com.innowise.Authentication_Service.service;

import com.innowise.Authentication_Service.dto.Customized.RegisterRequest;
import com.innowise.Authentication_Service.dto.UserResponse;

public interface RegistrationService {
    UserResponse register(RegisterRequest req);
}
