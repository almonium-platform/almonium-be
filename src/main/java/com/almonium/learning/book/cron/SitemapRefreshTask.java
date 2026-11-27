package com.almonium.learning.book.cron;

import com.almonium.learning.book.service.BookSitemapService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Rebuilds the sitemap shortly after start and then every hour, so a crawler is served a built document and a newly
 * published book appears within the hour. A sitemap without the processor's chapters (the processor was down, or
 * still on a release without the bulk endpoint while both deploy) is retried every five minutes instead. Each
 * application slot keeps its own copy.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.books.sitemap-scheduled", havingValue = "true", matchIfMissing = true)
public class SitemapRefreshTask {
    /** Ticks of five minutes between rebuilds of a sitemap that already lists its chapters. */
    static final int TICKS_PER_REBUILD = 12;

    private final BookSitemapService sitemapService;
    private int ticksSinceRebuild = TICKS_PER_REBUILD;

    @Scheduled(initialDelayString = "PT30S", fixedDelayString = "PT5M")
    public void tick() {
        ticksSinceRebuild++;
        if (ticksSinceRebuild < TICKS_PER_REBUILD && sitemapService.hasChapters()) return;
        sitemapService.refresh();
        ticksSinceRebuild = 0;
    }
}
