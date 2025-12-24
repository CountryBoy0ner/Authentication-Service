package com.innowise.Authentication_Service.Controller;

import com.innowise.Authentication_Service.dto.*;
import com.innowise.Authentication_Service.service.AuthService;
import com.innowise.Authentication_Service.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RegistrationService registrationService;


    @PostMapping("/register")
    public UserResponse register(@RequestBody com.innowise.Authentication_Service.dto.Customized.RegisterRequest request) {
        return registrationService.register(request);
    }

    //create token
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse tokens = authService.login(request);
        return ResponseEntity.ok(tokens);
    }

    //refresh
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse tokens = authService.refreshToken(request);
        return ResponseEntity.ok(tokens);
    }

    //validate
    @PostMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validate(@Valid @RequestBody ValidateTokenRequest request) {
        ValidateTokenResponse response = authService.validateToken(request);
        return ResponseEntity.ok(response);
    }
}
