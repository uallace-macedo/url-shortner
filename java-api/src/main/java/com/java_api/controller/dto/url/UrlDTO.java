package com.java_api.controller.dto.url;

import java.time.OffsetDateTime;

public record UrlDTO(
        Long id,
        String url,
        String customSlug,
        OffsetDateTime createdAt,
        OffsetDateTime expiresAt,
        Long clickCount,
        Long maxClickCount,
        Boolean active
) {}
