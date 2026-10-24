package com.almonium.learning.lexicon.service;

import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse;
import com.almonium.learning.lexicon.dto.response.LexiconSuggestion;
import com.almonium.learning.lexicon.mapper.LexiconEntryMapper;
import com.almonium.learning.lexicon.model.entity.AudioClip;
import com.almonium.learning.lexicon.model.entity.Lexeme;
import com.almonium.learning.lexicon.model.entity.LexemeForm;
import com.almonium.learning.lexicon.repository.AudioClipRepository;
import com.almonium.learning.lexicon.repository.LexemeFormRepository;
import com.almonium.learning.lexicon.repository.LexemeRepository;
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
 * is an inflected form of, each remembering which form matched.
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
            return Optional.empty();
        }
        return Optional.of(mapper.toResponse(language, text, matches, clipsOf(matches.keySet())));
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
    private Map<UUID, List<AudioClip>> clipsOf(Collection<Lexeme> lexemes) {
        List<UUID> ids = lexemes.stream().map(Lexeme::getId).toList();
        return clipRepository.findByLexemeIds(ids).stream()
                .collect(Collectors.groupingBy(clip -> clip.getPronunciation().getId()));
    }
}
