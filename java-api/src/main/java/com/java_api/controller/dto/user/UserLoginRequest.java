package com.java_api.controller.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank(message = "Please provide your email")
        @Email(message = "Please provide a valid email")
        String email,

        @NotBlank(message = "Please provide your password")
        String password
) {}
