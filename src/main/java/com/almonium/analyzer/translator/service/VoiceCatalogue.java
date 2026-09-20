package com.almonium.analyzer.translator.service;

import com.almonium.analyzer.translator.model.enums.LanguageVariety;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/** Named voices shipped with the application. Missing or disabled routes never borrow another variety's voice. */
@Component
public class VoiceCatalogue {
    private final Map<LanguageVariety, Voice> voices;

    public enum Provider {
        GOOGLE
    }

    public record Voice(
            LanguageVariety variety, Provider provider, boolean enabled, String languageCode, String voiceId) {}

    public VoiceCatalogue() throws IOException {
        this(readVoices());
    }

    VoiceCatalogue(List<Voice> entries) {
        Map<LanguageVariety, Voice> routes = new EnumMap<>(LanguageVariety.class);
        for (Voice voice : entries) {
            if (voice.variety() == null || voice.provider() == null) {
                throw new IllegalArgumentException("Each TTS route needs a variety and provider");
            }
            if (routes.putIfAbsent(voice.variety(), voice) != null) {
                throw new IllegalArgumentException(
                        "Duplicate TTS variety: " + voice.variety().getTag());
            }
            if (voice.enabled()) {
                String expectedCode =
                        switch (voice.variety()) {
                            case ZH_CN -> "cmn-CN";
                            case ZH_TW -> "cmn-TW";
                            default -> voice.variety().getTag();
                        };
                // Bare language tags may select a regional voice of that language; explicitly regional
                // varieties must match exactly. Mandarin uses Google's documented cmn provider alias.
                boolean matches = expectedCode.equals(voice.languageCode())
                        || (!expectedCode.contains("-")
                                && voice.languageCode() != null
                                && voice.languageCode().startsWith(expectedCode + "-"));
                if (!matches
                        || voice.voiceId() == null
                        || !voice.voiceId().startsWith(voice.languageCode() + "-")
                        || voice.voiceId()
                                .substring(voice.languageCode().length() + 1)
                                .isBlank()) {
                    throw new IllegalArgumentException(
                            "Invalid named TTS voice for " + voice.variety().getTag());
                }
            }
        }
        voices = Map.copyOf(routes);
    }

    private static List<Voice> readVoices() throws IOException {
        try (var input = new ClassPathResource("tts-voices.json").getInputStream()) {
            return List.of(new ObjectMapper().readValue(input, Voice[].class));
        }
    }

    public Voice requireVoice(LanguageVariety variety) {
        Voice voice = voices.get(variety);
        if (voice == null || !voice.enabled()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT, "Pronunciation is not available for " + variety.getTag() + ".");
        }
        return voice;
    }
}
