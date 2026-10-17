package com.almonium.learning.lexicon.service;

import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse;
import com.almonium.learning.lexicon.mapper.LexiconEntryMapper;
import com.almonium.learning.lexicon.model.entity.Lexeme;
import com.almonium.learning.lexicon.model.entity.LexemeForm;
import com.almonium.learning.lexicon.repository.LexemeFormRepository;
import com.almonium.learning.lexicon.repository.LexemeRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
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
        return Optional.of(mapper.toResponse(language, text, matches));
    }
}
