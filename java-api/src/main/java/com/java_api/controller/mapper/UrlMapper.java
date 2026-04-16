package com.java_api.controller.mapper;

import com.java_api.controller.dto.url.UrlDTO;
import com.java_api.model.Url;

public class UrlMapper {
    public static UrlDTO toDTO(Url url) {
        return new UrlDTO(
                url.getId(),
                url.getUrl(),
                url.getCustomSlug(),
                url.getCreatedAt(),
                url.getExpiresAt(),
                url.getClickCount(),
                url.getMaxClickCount(),
                url.getActive()
        );
    }

    public static Url toModel(UrlDTO urlDTO) {
        Url url = new Url();
        url.setId(urlDTO.id());
        url.setUrl(urlDTO.url());
        url.setCustomSlug(urlDTO.customSlug());
        url.setCreatedAt(urlDTO.createdAt());
        url.setExpiresAt(urlDTO.expiresAt());
        url.setClickCount(urlDTO.clickCount());
        url.setMaxClickCount(urlDTO.maxClickCount());
        url.setActive(urlDTO.active());
        return url;
    }
}
