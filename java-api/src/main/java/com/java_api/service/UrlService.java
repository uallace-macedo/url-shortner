package com.java_api.service;

import com.java_api.exception.custom.*;
import com.java_api.model.User;
import com.java_api.repository.UserRepository;
import com.java_api.utils.Base62Converter;
import com.java_api.model.Url;
import com.java_api.repository.UrlRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Transactional
    public Url create(UUID userId, Url url) {
        Optional<Url> exists = urlRepository.findByUrlAndUserId(url.getUrl(), userId);
        if(exists.isPresent()) {
            String msg = String.format("You already created a slug with '%s' url", url.getUrl());
            throw new SlugAlreadyCreatedException(msg);
        }

        String slug = "";
        if(url.getCustomSlug() != null) {
            Optional<Url> slugExists = urlRepository.findByCustomSlug(url.getCustomSlug());
            if(slugExists.isPresent()) {
                String msg = String.format("Slug '%s' already exists", url.getCustomSlug());
                throw new SlugAlreadyExistsException(msg);
            }

            slug = url.getCustomSlug();
        }

        if (url.getExpiresAt() != null) {
            OffsetDateTime fiveMin = OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(5);
            if(url.getExpiresAt().isBefore(fiveMin)) throw new InvalidUrlExpiresAtException("Expires at must be at least five minutes in the future");
        }

        long id = ((Number) entityManager
                .createNativeQuery("SELECT nextval('url_seq')")
                .getSingleResult()).longValue();

        if(slug.isEmpty()) {
            slug = Base62Converter.GenerateShortURL(id);
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        url.setId(id);
        url.setUser(user);
        url.setCustomSlug(slug);
        url.setExpiresAt(url.getExpiresAt());

        return urlRepository.save(url);
    }

    public Slice<Url> list(UUID userId, Pageable pageable) {
        return urlRepository.findByUserId(userId, pageable);
    }

    public void delete(UUID userId, String strId) {
        try {
            Long longId = Long.parseLong(strId);
            Url url = urlRepository.findById(longId).orElseThrow(() -> new UrlNotFoundException("URL not found"));
            if(!url.getUser().getId().equals(userId)) throw new InvalidUrlOwnershipException("This URL is not yours");
            urlRepository.delete(url);
        } catch (NumberFormatException e) {
            throw new InvalidUrlIdException("Please provide a valid URL id");
        }
    }
}