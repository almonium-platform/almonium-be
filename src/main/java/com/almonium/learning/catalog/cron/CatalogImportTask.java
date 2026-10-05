package com.almonium.learning.catalog.cron;

import com.almonium.learning.catalog.service.CatalogImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Keeps the catalogue fresh: a full import once a month and on the first start against an empty catalogue, and a
 * nightly recount of what is free where we make books, since a work's status only changes as the year turns. Both
 * application slots may run these during a rollout; the import is an idempotent upsert.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.catalog.scheduled", havingValue = "true", matchIfMissing = true)
public class CatalogImportTask {
    private final CatalogImportService catalogImportService;

    @EventListener(ApplicationReadyEvent.class)
    public void importWhenEmpty() {
        if (catalogImportService.isEmpty()) {
            log.info("Catalogue is empty; importing it now");
            catalogImportService.startImport();
        }
    }

    @Scheduled(cron = "0 30 4 2 * ?", zone = "UTC")
    public void monthlyImport() {
        catalogImportService.startImport();
    }

    @Scheduled(cron = "0 10 0 * * ?", zone = "UTC")
    public void nightlyStatus() {
        int changed = catalogImportService.refreshProductionStatus();
        if (changed > 0) {
            log.info("Production copyright status changed for {} catalogue works", changed);
        }
    }
}
