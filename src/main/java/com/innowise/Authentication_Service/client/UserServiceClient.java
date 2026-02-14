package com.innowise.Authentication_Service.client;

import com.innowise.Authentication_Service.dto.Customized.UserServiceCreateUserRequest;
import com.innowise.Authentication_Service.dto.Customized.UserServiceUserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserServiceClient {

    private final RestClient client;

    public UserServiceClient(@Value("${user-service.base-url}") String baseUrl) {
        this.client = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public UserServiceUserDto createUser(UserServiceCreateUserRequest req) {
        return client.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Roles", "ROLE_ADMIN")
                .header("X-User-Id", "0")  // Фиктивный ID пользователя
                .header("X-Username", "internal")  // Фиктивный username для внутреннего вызова
                .body(req)
                .retrieve()
                .body(UserServiceUserDto.class);
    }

    public void deleteUser(long id) {
        client.delete()
                .uri("/api/users/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}