package com.java_api.repository;

import com.java_api.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByUrlAndUserId(String url, UUID userId);
    Optional<Url> findByCustomSlug(String customSlug);
}
