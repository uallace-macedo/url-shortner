package com.java_api.controller;

import com.java_api.controller.dto.url.*;
import com.java_api.controller.mapper.UrlMapper;
import com.java_api.model.Url;
import com.java_api.service.ClickService;
import com.java_api.service.UrlService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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
    private final ClickService clickService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PageResponseDTO<UrlDTO>> list(@PageableDefault(size = 10) Pageable pageable, Authentication auth) {
        UUID userId = getUserId(auth);

        Slice<UrlDTO> slice = urlService.list(userId, pageable)
                .map(url -> new UrlDTO(
                        url.getId(),
                        url.getUrl(),
                        url.getCustomSlug(),
                        url.getCreatedAt(),
                        url.getExpiresAt(),
                        url.getClickCount(),
                        url.getMaxClickCount(),
                        url.getActive()
                ));

        return ResponseEntity.ok(
                new PageResponseDTO<>(slice)
        );
    }

    @GetMapping(
            value = "/{id}/stats",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getStats(
            @PathVariable("id") String id,
            Authentication auth
    ) {
        UUID userId = getUserId(auth);
        UrlStatsDTO urlStatsDTO = clickService.getStats(id, userId);
        return ResponseEntity.ok(urlStatsDTO);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> create(@Valid @RequestBody UrlRequestDTO urlRequestDTO, Authentication auth) {
        UUID userId = getUserId(auth);

        Url url = new Url();
        url.setUrl(urlRequestDTO.url());
        url.setCustomSlug(urlRequestDTO.customSlug());
        url.setMaxClickCount(urlRequestDTO.maxClickCount());
        url.setExpiresAt(urlRequestDTO.expiresAt());

        Url newUrl = urlService.create(userId, url);
        return ResponseEntity.status(HttpStatus.CREATED).body(UrlMapper.toDTO(newUrl));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id, Authentication auth) {
        UUID userId = getUserId(auth);
        urlService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> patch(
            @PathVariable("id") String id,
            @Valid @RequestBody UrlChangeSlugDTO urlChangeSlugDTO,
            Authentication auth
    ) {
        UUID userId = getUserId(auth);
        urlService.patch(userId, id, urlChangeSlugDTO.newSlug());
        return ResponseEntity.noContent().build();
    }

    private UUID getUserId(Authentication auth) {
        String subj = auth.getName();
        return UUID.fromString(subj);
    }
}
