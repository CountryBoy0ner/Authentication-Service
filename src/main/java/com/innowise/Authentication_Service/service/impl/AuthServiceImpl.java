package com.innowise.Authentication_Service.service.impl;

import com.innowise.Authentication_Service.auth.dto.RegisterRequest;
import com.innowise.Authentication_Service.auth.dto.UserResponse;
import com.innowise.Authentication_Service.model.Role;
import com.innowise.Authentication_Service.model.User;
import com.innowise.Authentication_Service.repository.RoleRepository;
import com.innowise.Authentication_Service.repository.UserRepository;
import com.innowise.Authentication_Service.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
}
