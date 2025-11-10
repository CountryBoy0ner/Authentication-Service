package com.innowise.Authentication_Service.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidateTokenRequest {

    @NotBlank
    private String token;
}