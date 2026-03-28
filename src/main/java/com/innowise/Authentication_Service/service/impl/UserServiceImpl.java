package com.innowise.Authentication_Service.service.impl;

import com.innowise.Authentication_Service.dto.UserDto;
import com.innowise.Authentication_Service.model.User;
import com.innowise.Authentication_Service.repository.UserRepository;
import com.innowise.Authentication_Service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.innowise.Authentication_Service.model.Role;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;

    @Override
    public UserDto get(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .enabled(user.isEnabled())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .toList())
                .build();
    }

    @Override
    public Page<UserDto> getAll(Pageable pageable) {
        return userRepo.findAll(pageable)
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .enabled(user.isEnabled())
                        .roles(user.getRoles().stream()
                                .map(Role::getName)
                                .toList())
                        .build());
    }

}
