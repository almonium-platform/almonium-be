package com.almonium.learning.cover.cron;

import com.almonium.learning.cover.service.CoverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** Each night, the works that joined the shelf since the last run get their cover looked for. */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "covers.scheduled", havingValue = "true", matchIfMissing = true)
public class CoverTask {
    private final CoverService coverService;

    @Scheduled(cron = "0 40 3 * * ?", zone = "UTC")
    public void nightly() {
        int prepared = coverService.prepareMissing();
        if (prepared > 0) {
            log.info("Looked for cover art for {} works", prepared);
        }
    }
}
