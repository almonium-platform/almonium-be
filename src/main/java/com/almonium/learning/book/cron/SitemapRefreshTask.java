package com.almonium.learning.book.cron;

import com.almonium.learning.book.service.BookSitemapService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Rebuilds the sitemap shortly after start and every hour, so a crawler is served a built document and a newly
 * published book appears within the hour. Each application slot keeps its own copy.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.books.sitemap-scheduled", havingValue = "true", matchIfMissing = true)
public class SitemapRefreshTask {
    private final BookSitemapService sitemapService;

    @Scheduled(initialDelayString = "PT30S", fixedDelayString = "PT1H")
    public void refresh() {
        sitemapService.refresh();
    }
}
