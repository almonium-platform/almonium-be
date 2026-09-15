package com.almonium.analyzer.translator.provider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class TranslationProviderRegistry {
    private final Map<String, TranslationProvider> providers;

    public TranslationProviderRegistry(List<TranslationProvider> providers) {
        Map<String, TranslationProvider> indexedProviders = new LinkedHashMap<>();
        for (TranslationProvider provider : providers) {
            String providerName = normalize(provider.providerName());
            TranslationProvider duplicate = indexedProviders.putIfAbsent(providerName, provider);
            if (duplicate != null) {
                throw new IllegalStateException("Duplicate translation provider: " + providerName);
            }
        }
        this.providers = Map.copyOf(indexedProviders);
    }

    public Optional<TranslationProvider> find(String providerName) {
        return Optional.ofNullable(providers.get(normalize(providerName)));
    }

    private static String normalize(String providerName) {
        if (providerName == null || providerName.isBlank()) {
            throw new IllegalArgumentException("Translation provider name must not be blank");
        }
        return providerName.toUpperCase(Locale.ROOT);
    }
}
