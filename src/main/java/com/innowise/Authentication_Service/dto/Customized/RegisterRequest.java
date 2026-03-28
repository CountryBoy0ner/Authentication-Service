package com.innowise.Authentication_Service.dto.Customized;

import java.time.LocalDate;

public class RegisterRequest {
    public String username;
    public String password;

    public String email;
    public String name;
    public String surname;
    public LocalDate birthDate;
}
