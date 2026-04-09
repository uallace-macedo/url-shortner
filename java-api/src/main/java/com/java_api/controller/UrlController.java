package com.java_api.controller;

import com.java_api.dto.UrlDTO;
import com.java_api.dto.UrlRequestDTO;
import com.java_api.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UrlDTO> create(@Valid @RequestBody UrlRequestDTO urlRequestDTO) {
        UrlDTO createdUrl = urlService.create(urlRequestDTO.url());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUrl);
    }

}
