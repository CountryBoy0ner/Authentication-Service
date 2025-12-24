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

        // create user in user-service
        UserServiceCreateUserRequest createReq = new UserServiceCreateUserRequest();
        createReq.email = req.email;
        createReq.name = req.name;
        createReq.surname = req.surname;
        createReq.birthDate = req.birthDate;

        UserServiceUserDto createdUser = userServiceClient.createUser(createReq);

        // 2) сохранить credentials в auth-db
        try {
            credentialsService.createCredentials(
                    createdUser.id,
                    req.username,
                    req.password
            );
        } catch (Exception e) {
            // 3) rollback:
            try {
                userServiceClient.deleteUser(createdUser.id);
            } catch (Exception rollbackEx) {
                System.out.println(rollbackEx.getMessage());

            }
            throw e;
        }

        return UserResponse.builder()
                .id(createdUser.id)      // <-- ID из user-service
                .username(req.username)
                .enabled(true)
                .build();
    }
}
