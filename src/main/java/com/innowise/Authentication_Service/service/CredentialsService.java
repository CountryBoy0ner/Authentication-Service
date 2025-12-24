package com.innowise.Authentication_Service.service;

public interface CredentialsService {
    void createCredentials(Long userId, String username, String rawPassword);
}
