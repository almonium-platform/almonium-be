package com.almonium.analyzer.analyzer.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.dto.MLTranslationCard;
import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.analyzer.translator.repository.TranslatorRepository;
import com.almonium.analyzer.translator.service.TranslationEngine;
import com.almonium.analyzer.translator.service.TranslationService;
import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LanguageProcessor {
    TranslationService googleService;
    TranslatorRepository translatorRepository;
    TranslationEngine translationEngine;

    public MLTranslationCard bulkTranslate(String text, Language targetLang) {
        // todo deepL
        return new MLTranslationCard(
                translatorRepository.getGoogle().getName(), googleService.bulkTranslateText(text, targetLang.name()));
    }

    public TranslationCardDto translate(String entry, Language sourceLang, Language targetLang) {
        return translationEngine.translate(entry, sourceLang, targetLang);
    }

    public ByteString textToSpeech(String code, String text) {
        return googleService.textToSpeech(code, text);
    }
}
