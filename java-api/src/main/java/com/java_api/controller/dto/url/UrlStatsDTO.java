package com.java_api.controller.dto.url;

import com.java_api.controller.dto.click.DailyClicksDTO;
import com.java_api.controller.dto.click.ReferrerStatsDTO;

import java.util.List;

public record UrlStatsDTO(
    Long urlId,
    String url,
    String customSlug,
    Long totalClicks,
    List<DailyClicksDTO> clicksPerDay,
    List<ReferrerStatsDTO> topReferrers
) {}
