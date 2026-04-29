package com.java_api.scheduler;

import com.java_api.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class UrlScheduler {
    private final UrlRepository urlRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void removeOutdatedUrls() {
        urlRepository.deleteByExpiresAtBefore(OffsetDateTime.now());
        urlRepository.deleteByLastClickedAtBefore(OffsetDateTime.now().minusDays(15));
    }
}
