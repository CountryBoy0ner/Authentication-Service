package com.innowise.Authentication_Service.Controller;

import com.innowise.Authentication_Service.dto.UserDto;
import com.innowise.Authentication_Service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(
            @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        if (userId == null) {
            throw new AccessDeniedException("Authentication required");
        }
        return ResponseEntity.ok(userService.get(userId));
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAll(
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            @RequestHeader(value = "X-Roles", required = false) String roles
    ) {
        if (roles == null || !roles.contains("ROLE_ADMIN")) {
            throw new AccessDeniedException("Admin role required");
        }
        return ResponseEntity.ok(userService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(
            @PathVariable Long id,
            @RequestHeader(value = "X-Roles", required = false) String roles
    ) {
        if (roles == null || !roles.contains("ROLE_ADMIN")) {
            throw new AccessDeniedException("Admin role required");
        }
        return ResponseEntity.ok(userService.get(id));
    }
}