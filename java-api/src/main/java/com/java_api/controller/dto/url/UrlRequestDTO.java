package com.java_api.controller.dto.url;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;

import java.time.OffsetDateTime;

public record UrlRequestDTO(
        @NotBlank(message = "URL cannot be void")
        String url,

        @Size(min = 5, max = 30, message = "Custom slug must be between 5 & 30 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_-]{5,30}$", message = "Slug must be between 3 & 30 characters and contains only letters, numbers, '-' or '_'")
        @JsonProperty("custom_slug")
        String customSlug,

        @JsonProperty("expires_at")
        OffsetDateTime expiresAt,

        @JsonProperty("max_click_count")
        Long maxClickCount
) {}
