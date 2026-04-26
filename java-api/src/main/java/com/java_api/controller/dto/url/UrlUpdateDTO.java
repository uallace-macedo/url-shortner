package com.java_api.controller.dto.url;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;

public record UrlUpdateDTO(
        @Pattern(regexp = "^[a-zA-Z0-9_-]{3,30}$", message = "Slug must be between 3 & 30 characters and contains only letters, numbers, '-' or '_'")
        @JsonProperty("new_slug")
        String newSlug,

        @JsonProperty("max_click_count")
        Long maxClickCount,

        Boolean active
) {}
