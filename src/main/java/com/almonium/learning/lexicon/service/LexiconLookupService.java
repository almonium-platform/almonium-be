package com.almonium.learning.lexicon.service;

import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.HomophoneView;
import com.almonium.learning.lexicon.dto.response.LexiconSuggestion;
import com.almonium.learning.lexicon.mapper.LexiconEntryMapper;
import com.almonium.learning.lexicon.model.entity.AudioClip;
import com.almonium.learning.lexicon.model.entity.Lexeme;
import com.almonium.learning.lexicon.model.entity.LexemeForm;
import com.almonium.learning.lexicon.repository.AudioClipRepository;
import com.almonium.learning.lexicon.repository.LexemeFormRepository;
import com.almonium.learning.lexicon.repository.LexemeRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Resolves a reader's word to its lexemes: those it names directly, by headword or lemma, come first; then those it
 * is an inflected form of, each remembering which form matched. A word that is neither is looked for among the
 * spellings other entries answer for (colour under color, signed under sign).
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LexiconLookupService {
    private final LexemeRepository lexemeRepository;
    private final LexemeFormRepository formRepository;
    private final AudioClipRepository clipRepository;
    private final LexiconEntryMapper mapper;

    public Optional<LexiconEntryResponse> lookup(String language, String text) {
        String normalized = WordNormalizer.normalize(text);
        Map<Lexeme, LexemeForm> matches = new LinkedHashMap<>();
        for (Lexeme lexeme : lexemeRepository.findByLanguageAndNormalizedWord(language, normalized)) {
            matches.put(lexeme, null);
        }
        for (LexemeForm form : formRepository.findByLanguageAndNormalizedFormOrderByPosition(language, normalized)) {
            if (!matches.containsKey(form.getLexeme())) {
                matches.put(form.getLexeme(), form);
            }
        }
        if (matches.isEmpty()) {
            for (Lexeme lexeme : lexemeRepository.findByLanguageAndAlias(language, normalized)) {
                matches.put(lexeme, null);
            }
        }
        if (matches.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
                mapper.toResponse(language, text, matches, clipsOf(matches.keySet()), homophonesOf(matches)));
    }

    /** Languages other than {@code excludedLanguage} whose lexicon answers for {@code text}, best match first. */
    public List<String> matchingLanguages(String excludedLanguage, String text) {
        String normalized = WordNormalizer.normalize(text);
        if (normalized.isEmpty()) {
            return List.of();
        }
        return lexemeRepository.findMatchingLanguages(excludedLanguage, normalized);
    }

    /** Up to {@code limit} headwords beginning with {@code prefix}, glossed in {@code fluent} where the entry can. */
    public List<LexiconSuggestion> suggest(String language, String prefix, String fluent, int limit) {
        String escaped = WordNormalizer.normalize(prefix).replaceAll("([\\\\%_])", "\\\\$1");
        return lexemeRepository.suggest(language, escaped + "%", fluent, limit).stream()
                .map(row -> new LexiconSuggestion(
                        row.getHeadword(),
                        row.getPos(),
                        row.getGender(),
                        row.getGloss(),
                        row.getZipf() == null ? null : row.getZipf().doubleValue()))
                .toList();
    }

    /** Every clip of these lexemes in one query, by pronunciation, so the mapper never asks per pronunciation. */
    /**
     * Words said like the lexemes the reader's word names directly, by lexeme, with the varieties that say them
     * alike. A lexeme reached through one of its forms gets none: saw sounds like nothing see does.
     */
    private Map<UUID, List<HomophoneView>> homophonesOf(Map<Lexeme, LexemeForm> matches) {
        List<UUID> named = matches.entrySet().stream()
                .filter(match -> match.getValue() == null)
                .map(match -> match.getKey().getId())
                .toList();
        if (named.isEmpty()) {
            return Map.of();
        }
        record Word(String lexeme, String lemma, String pos) {}
        Map<Word, List<String>> varieties = new LinkedHashMap<>();
        for (LexemeRepository.SpokenAlike row : lexemeRepository.findSpokenAlike(named)) {
            varieties
                    .computeIfAbsent(new Word(row.getLexeme(), row.getLemma(), row.getPos()), word -> new ArrayList<>())
                    .add(row.getVariety());
        }
        Map<UUID, List<HomophoneView>> homophones = new LinkedHashMap<>();
        varieties.forEach((word, said) -> homophones
                .computeIfAbsent(UUID.fromString(word.lexeme()), lexeme -> new ArrayList<>())
                .add(new HomophoneView(word.lemma(), word.pos(), List.copyOf(said))));
        return homophones;
    }

    private Map<UUID, List<AudioClip>> clipsOf(Collection<Lexeme> lexemes) {
        List<UUID> ids = lexemes.stream().map(Lexeme::getId).toList();
        return clipRepository.findByLexemeIds(ids).stream()
                .collect(Collectors.groupingBy(clip -> clip.getPronunciation().getId()));
    }
}
