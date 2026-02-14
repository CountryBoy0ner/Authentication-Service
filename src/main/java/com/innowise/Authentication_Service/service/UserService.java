package com.innowise.Authentication_Service.service;


import com.innowise.Authentication_Service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto get(Long id);
    Page<UserDto> getAll(Pageable pageable);
}
