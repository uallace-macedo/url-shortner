package com.java_api.controller;

import com.java_api.controller.dto.url.UrlRequestDTO;
import com.java_api.controller.mapper.UrlMapper;
import com.java_api.model.Url;
import com.java_api.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> create(@Valid @RequestBody UrlRequestDTO urlRequestDTO, Authentication auth) {
        System.out.println("DTO: " + urlRequestDTO);
        String subj = auth.getName();
        UUID userId = UUID.fromString(subj);

        Url url = new Url();
        url.setUrl(urlRequestDTO.url());
        url.setCustomSlug(urlRequestDTO.customSlug());
        url.setMaxClickCount(urlRequestDTO.maxClickCount());
        url.setExpiresAt(urlRequestDTO.expiresAt());

        Url newUrl = urlService.create(userId, url);
        return ResponseEntity.status(HttpStatus.CREATED).body(UrlMapper.toDTO(newUrl));
    }
}
