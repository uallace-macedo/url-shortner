package com.java_api.service;

import com.java_api.controller.dto.click.DailyClicksDTO;
import com.java_api.controller.dto.click.ReferrerStatsDTO;
import com.java_api.controller.dto.url.UrlStatsDTO;
import com.java_api.exception.custom.url.InvalidUrlIdException;
import com.java_api.exception.custom.url.UrlForbiddenException;
import com.java_api.exception.custom.url.UrlNotFoundException;
import com.java_api.model.Click;
import com.java_api.model.Url;
import com.java_api.repository.ClickRepository;
import com.java_api.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClickService {
    private final ClickRepository clickRepository;
    private final UrlRepository urlRepository;

    public UrlStatsDTO getStats(String urlId, UUID userId) {
        Url url = verifyUrlById(userId, urlId);

        List<Click> clicks = clickRepository.findByUrlId(url.getId());
        Long totalClicks = (long) clicks.size();

        List<DailyClicksDTO> clicksPerDay = clicks.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getClickedAt().toLocalDate(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(e -> new DailyClicksDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(DailyClicksDTO::date))
                .toList();

        List<ReferrerStatsDTO> topReferrers = clicks.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getReferrer() == null || c.getReferrer().isBlank()
                                ? "direct"
                                : extractDomain(c.getReferrer()),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(e -> new ReferrerStatsDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(ReferrerStatsDTO::count).reversed())
                .limit(5)
                .toList();

        return new UrlStatsDTO(
                url.getId(),
                url.getUrl(),
                url.getCustomSlug(),
                totalClicks,
                clicksPerDay,
                topReferrers
        );
    }

    private Url verifyUrlById(UUID userId, String strId) {
        try {
            Long longId = Long.parseLong(strId);
            Url url = urlRepository.findById(longId).orElseThrow(() -> new UrlNotFoundException("URL not found"));
            if(!url.getUser().getId().equals(userId)) throw new UrlForbiddenException("This URL is not yours");
            return url;
        } catch(NumberFormatException e) {
            throw new InvalidUrlIdException("Please provide a valid URL id");
        }
    }

    private String extractDomain(String referrer) {
        try {
            return new URI(referrer).getHost();
        } catch (Exception e) {
            return referrer;
        }
    }
}
