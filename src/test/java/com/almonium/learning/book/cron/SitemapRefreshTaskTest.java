package com.almonium.learning.book.cron;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.learning.book.service.BookSitemapService;
import org.junit.jupiter.api.Test;

/** An hourly rebuild of a good sitemap; a sitemap without chapters is retried on every five-minute tick. */
class SitemapRefreshTaskTest {
    BookSitemapService sitemap = mock(BookSitemapService.class);
    SitemapRefreshTask task = new SitemapRefreshTask(sitemap);

    @Test
    void buildsOnTheFirstTickThenHourlyWhileTheSitemapListsItsChapters() {
        when(sitemap.hasChapters()).thenReturn(true);

        for (int tick = 0; tick < 1 + SitemapRefreshTask.TICKS_PER_REBUILD; tick++) task.tick();

        verify(sitemap, times(2)).refresh();
    }

    @Test
    void retriesEveryTickWhileTheSitemapHasNoChapters() {
        when(sitemap.hasChapters()).thenReturn(false);

        for (int tick = 0; tick < 3; tick++) task.tick();

        verify(sitemap, times(3)).refresh();
    }
}
