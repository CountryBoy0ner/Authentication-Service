package com.innowise.Authentication_Service.service.impl;

import com.innowise.Authentication_Service.model.Role;
import com.innowise.Authentication_Service.model.User;
import com.innowise.Authentication_Service.repository.RoleRepository;
import com.innowise.Authentication_Service.repository.UserRepository;
import com.innowise.Authentication_Service.service.CredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CredentialsServiceImpl implements CredentialsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createCredentials(Long userId, String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already taken");
        }

        Role userRole = roleRepository
                .findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

        User user = User.builder()
                .userId(userId)
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .enabled(true)
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);
    }
}
