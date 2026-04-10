package com.java_api.dto.url;

import jakarta.validation.constraints.NotBlank;

public record UrlRequestDTO(
        @NotBlank(message = "URL cannot be void")
        String url
) {}
