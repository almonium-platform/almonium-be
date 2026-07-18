package com.almonium.analyzer.translator.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.analyzer.translator.provider.TranslationProvider;
import com.almonium.analyzer.translator.provider.TranslationProviderRegistry;
import com.almonium.analyzer.translator.repository.LangPairTranslatorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TranslationEngine {
    LangPairTranslatorRepository langPairTranslatorRepository;
    TranslationProviderRegistry providerRegistry;

    public TranslationCardDto translate(String entry, Language sourceLanguage, Language targetLanguage) {
        if (sourceLanguage == null || targetLanguage == null) {
            return null;
        }

        List<String> providerNames =
                langPairTranslatorRepository.findProviderNames(sourceLanguage.name(), targetLanguage.name());
        for (String providerName : providerNames) {
            TranslationProvider provider = providerRegistry.find(providerName).orElse(null);
            if (provider == null) {
                log.warn("Translation provider {} is configured but not implemented", providerName);
                continue;
            }

            try {
                return provider.translate(entry, sourceLanguage, targetLanguage);
            } catch (ApiIntegrationException exception) {
                log.warn(
                        "Translation provider {} failed; trying the next configured provider", providerName, exception);
            }
        }
        return null;
    }
}
