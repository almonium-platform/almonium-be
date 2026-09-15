package com.almonium.analyzer.translator.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;

class TranslationProviderRegistryTest {
    @Test
    void findsProviderCaseInsensitively() {
        TranslationProvider provider = providerNamed("Yandex");
        TranslationProviderRegistry registry = new TranslationProviderRegistry(List.of(provider));

        assertThat(registry.find("yandex")).contains(provider);
    }

    @Test
    void rejectsDuplicateProviderNames() {
        TranslationProvider first = providerNamed("YANDEX");
        TranslationProvider second = providerNamed("yandex");

        assertThatThrownBy(() -> new TranslationProviderRegistry(List.of(first, second)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate translation provider");
    }

    private TranslationProvider providerNamed(String name) {
        TranslationProvider provider = mock(TranslationProvider.class);
        when(provider.providerName()).thenReturn(name);
        return provider;
    }
}
