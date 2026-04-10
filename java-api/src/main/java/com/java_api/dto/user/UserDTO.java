package com.java_api.dto.user;

import java.time.LocalDate;
import java.util.UUID;

public record UserDTO(UUID id, String name, String email, LocalDate createdAt) {}
