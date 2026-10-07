package com.almonium.learning.catalog.controller.open;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.catalog.service.CatalogSearchService;
import org.junit.jupiter.api.Test;

class CatalogOpenControllerTest {
    @Test
    void passesTheReadingLanguageSoHitsCarryWantCounts() {
        CatalogSearchService service = mock(CatalogSearchService.class);

        new CatalogOpenController(service).search("verne", 20, Language.FR);

        verify(service).search("verne", 20, Language.FR);
    }
}
