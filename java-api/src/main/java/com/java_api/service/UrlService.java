package com.java_api.service;

import com.java_api.utils.Base62Converter;
import com.java_api.dto.UrlDTO;
import com.java_api.mapper.UrlMapper;
import com.java_api.model.Url;
import com.java_api.repository.UrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final EntityManager entityManager;

    @Transactional
    public UrlDTO create(String url) {
        Optional<Url> exists = urlRepository.findByUrl(url);
        if(exists.isPresent()) return UrlMapper.toDTO(exists.get());

        long id = ((Number) entityManager
                .createNativeQuery("SELECT nextval('url_seq')")
                .getSingleResult()).longValue();

        String shortUrl = Base62Converter.GenerateShortURL(id);
        Url newUrl = new Url(id, url, shortUrl);
        urlRepository.save(newUrl);

        return UrlMapper.toDTO(newUrl);
    }
}