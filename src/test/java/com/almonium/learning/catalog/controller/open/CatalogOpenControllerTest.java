package com.almonium.learning.catalog.controller.open;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.catalog.service.CatalogSearchService;
import com.almonium.learning.catalog.service.TextSources;
import org.junit.jupiter.api.Test;

class CatalogOpenControllerTest {
    final CatalogSearchService service = mock(CatalogSearchService.class);
    final CatalogOpenController controller = new CatalogOpenController(service, mock(TextSources.class));

    @Test
    void passesTheReadingLanguageSoHitsCarryWantCounts() {
        controller.search("verne", 20, Language.FR);

        verify(service).search("verne", 20, Language.FR);
    }

    @Test
    void publicDomainDayDefaultsToNextYear() {
        when(service.currentYear()).thenReturn(2026);

        controller.comingFree(null, 12, Language.DE);
        controller.comingFree(2026, 5, null);

        verify(service).freeFrom(2027, 12, Language.DE);
        verify(service).freeFrom(2026, 5, null);
    }
}
