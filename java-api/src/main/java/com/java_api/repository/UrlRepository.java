package com.java_api.repository;

import com.java_api.model.Url;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByUrlAndUserId(String url, UUID userId);
    Slice<Url> findByUserId(UUID userId, Pageable pageable);
    boolean existsByCustomSlug(String customSlug);
    void deleteByExpiresAtBefore(OffsetDateTime today);
    void deleteByLastClickedAtBefore(OffsetDateTime date);
}
