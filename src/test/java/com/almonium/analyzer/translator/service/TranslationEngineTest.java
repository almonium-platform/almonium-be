package com.almonium.analyzer.translator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.analyzer.translator.provider.TranslationProvider;
import com.almonium.analyzer.translator.provider.TranslationProviderRegistry;
import com.almonium.analyzer.translator.repository.LangPairTranslatorRepository;
import java.util.List;
import org.junit.jupiter.api.Test;

class TranslationEngineTest {
    @Test
    void usesProvidersInConfiguredPriorityOrder() {
        TranslationProvider primary = providerNamed("PRIMARY");
        TranslationProvider fallback = providerNamed("FALLBACK");
        TranslationCardDto expected = new TranslationCardDto();
        when(primary.translate("word", Language.EN, Language.DE))
                .thenThrow(new ApiIntegrationException("temporarily unavailable"));
        when(fallback.translate("word", Language.EN, Language.DE)).thenReturn(expected);

        LangPairTranslatorRepository repository = mock(LangPairTranslatorRepository.class);
        when(repository.findProviderNames("EN", "DE")).thenReturn(List.of("PRIMARY", "FALLBACK"));
        TranslationProviderRegistry registry = new TranslationProviderRegistry(List.of(primary, fallback));
        TranslationEngine engine = new TranslationEngine(repository, registry);

        assertThat(engine.translate("word", Language.EN, Language.DE)).isSameAs(expected);
    }

    @Test
    void ignoresConfiguredProviderWithoutImplementation() {
        LangPairTranslatorRepository repository = mock(LangPairTranslatorRepository.class);
        when(repository.findProviderNames("EN", "DE")).thenReturn(List.of("NOT_IMPLEMENTED"));
        TranslationEngine engine = new TranslationEngine(repository, new TranslationProviderRegistry(List.of()));

        assertThat(engine.translate("word", Language.EN, Language.DE)).isNull();
    }

    private TranslationProvider providerNamed(String name) {
        TranslationProvider provider = mock(TranslationProvider.class);
        when(provider.providerName()).thenReturn(name);
        return provider;
    }
}
