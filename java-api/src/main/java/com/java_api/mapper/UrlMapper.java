package com.java_api.mapper;

import com.java_api.dto.url.UrlDTO;
import com.java_api.model.Url;

public class UrlMapper {
    public static UrlDTO toDTO(Url url) {
        return new UrlDTO(
                url.getId(),
                url.getUrl(),
                url.getShortUrl()
        );
    }

    public static Url toModel(UrlDTO urlDTO) {
        return new Url(
                urlDTO.id(),
                urlDTO.url(),
                urlDTO.shortUrl(),
                null
        );
    }
}
