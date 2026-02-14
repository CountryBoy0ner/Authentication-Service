package com.innowise.Authentication_Service.service.impl;

import com.innowise.Authentication_Service.client.UserServiceClient;
import com.innowise.Authentication_Service.dto.Customized.RegisterRequest;
import com.innowise.Authentication_Service.dto.Customized.UserServiceCreateUserRequest;
import com.innowise.Authentication_Service.dto.Customized.UserServiceUserDto;
import com.innowise.Authentication_Service.dto.UserResponse;
import com.innowise.Authentication_Service.service.CredentialsService;
import com.innowise.Authentication_Service.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserServiceClient userServiceClient;
    private final CredentialsService credentialsService;

    @Override
    public UserResponse register(RegisterRequest req) {

        Long userId;
        try {
            userId = credentialsService.createCredentials(req.username, req.password);
        } catch (Exception e) {
            throw e;
        }

        UserServiceCreateUserRequest createReq = new UserServiceCreateUserRequest();
        createReq.id = userId;
        createReq.email = req.email;
        createReq.name = req.name;
        createReq.surname = req.surname;
        createReq.birthDate = req.birthDate;

        System.out.println("Trying to create user with ID: " + userId);

        try {
            UserServiceUserDto createdUser = userServiceClient.createUser(createReq);
            if (!createdUser.id.equals(userId)) {
                throw new RuntimeException("ID mismatch after creation");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                credentialsService.deleteCredentials(userId);
            } catch (Exception rollbackEx) {
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
            throw e;
        }

        return UserResponse.builder()
                .id(userId)
                .username(req.username)
                .enabled(true)
                .build();
    }
}