package com.almonium.analyzer.translator.service;

import com.almonium.analyzer.translator.dto.VoiceAvailabilityDto;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.analyzer.translator.model.enums.LanguageVariety;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
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

    public enum Gender {
        MALE,
        FEMALE,
        NEUTRAL
    }

    public record Voice(
            LanguageVariety variety,
            Provider provider,
            boolean enabled,
            String languageCode,
            String voiceId,
            Gender gender) {}

    public VoiceCatalogue() throws IOException {
        this(readVoices());
        for (LanguageVariety variety : LanguageVariety.values()) {
            if (!voices.containsKey(variety)) {
                throw new IllegalArgumentException("Missing TTS route: " + variety.getTag());
            }
        }
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
                            case NO -> "nb-NO";
                            case TL -> "fil-PH";
                            default -> voice.variety().getTag();
                        };
                // Bare language tags may select a regional voice of that language; explicitly regional
                // varieties must match exactly. Mandarin uses Google's documented cmn provider alias.
                boolean matches = expectedCode.equals(voice.languageCode())
                        || (!expectedCode.contains("-")
                                && voice.languageCode() != null
                                && voice.languageCode().startsWith(expectedCode + "-"));
                if (!matches
                        || voice.gender() == null
                        || voice.voiceId() == null
                        // Google publishes Filipino Neural2 IDs with lowercase "ph".
                        || !voice.voiceId()
                                .regionMatches(
                                        true,
                                        0,
                                        voice.languageCode() + "-",
                                        0,
                                        voice.languageCode().length() + 1)
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

    /** Ordered by language enum, then variety display order. No provider requests are made. */
    public List<VoiceAvailabilityDto> list(Language language) {
        return Arrays.stream(Language.values())
                .filter(candidate -> language == null || candidate == language)
                .flatMap(candidate -> LanguageVariety.forLanguage(candidate).stream())
                .map(variety -> {
                    Voice voice = voices.get(variety);
                    boolean available = voice != null && voice.enabled();
                    return new VoiceAvailabilityDto(
                            variety.getLanguage(),
                            variety,
                            LanguageVariety.defaultFor(variety.getLanguage()).orElseThrow() == variety,
                            available,
                            available ? null : "NO_ENABLED_VOICE",
                            available ? voice.provider() : null,
                            available ? voice.languageCode() : null,
                            available ? voice.voiceId() : null,
                            available ? voice.gender() : null);
                })
                .toList();
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
