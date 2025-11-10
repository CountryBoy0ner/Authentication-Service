package com.innowise.Authentication_Service.Controller;


import com.innowise.Authentication_Service.auth.dto.UserResponse;
import com.innowise.Authentication_Service.model.Role;
import com.innowise.Authentication_Service.model.User;
import com.innowise.Authentication_Service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public UserResponse me(Authentication auth) {
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .enabled(user.isEnabled())
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> UserResponse.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .enabled(u.isEnabled())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id, Authentication auth) {
        String currentUsername = auth.getName();
        User current = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AccessDeniedException("User not found"));
        boolean isAdmin = hasRole(current.getRoles(), "ROLE_ADMIN");
        boolean isOwner = current.getId().equals(id);
        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You can access only your own user info");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AccessDeniedException("User not found"));
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .enabled(user.isEnabled())
                .build();
    }

    private boolean hasRole(Set<Role> roles, String roleName) {
        return roles.stream().anyMatch(r -> r.getName().equals(roleName));
    }
}
