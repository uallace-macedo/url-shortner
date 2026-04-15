package com.java_api.controller.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserCreateRequest(
        @NotBlank(message = "Please provide a name")
        @Length(min = 4, message = "Name must be at least 4 characters")
        String name,

        @Email(message = "Please provide a valid email")
        @NotBlank(message = "Please provide an email")
        String email,

        @NotBlank(message = "Please provide a password")
        @Length(min = 8, message = "Password must have at least 8 characters")
        String password
) {}
