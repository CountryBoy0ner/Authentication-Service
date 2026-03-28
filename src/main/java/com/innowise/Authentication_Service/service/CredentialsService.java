package com.innowise.Authentication_Service.service;

public interface CredentialsService {
    Long createCredentials(String username, String rawPassword);
    void deleteCredentials(Long id);
}
