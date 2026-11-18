package com.almonium.learning.lexicon.service;

import com.almonium.card.core.repository.LearningItemRepository;
import com.almonium.learning.lexicon.dto.response.SenseHistoryResponse;
import com.almonium.learning.lexicon.dto.response.SenseHistoryResponse.SenseVersion;
import com.almonium.learning.lexicon.model.entity.EntryVersion;
import com.almonium.learning.lexicon.repository.EntryVersionRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Everything published about one sense, found by its key: each entry version that had it, with the sense as it
 * stood there, and the version that dropped it, which is the next version of the headword when that one no longer
 * has the sense. How many cards were saved from the sense comes along, so an editor sees who a change would reach.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SenseHistoryService {
    private final EntryVersionRepository entryVersionRepository;
    private final LearningItemRepository learningItemRepository;

    public SenseHistoryResponse history(UUID key) {
        String live = "{\"lexemes\": [{\"senses\": [{\"id\": \"" + key + "\"}]}]}";
        List<EntryVersion> having = entryVersionRepository.findContaining(live);
        List<SenseVersion> versions = new ArrayList<>();
        for (int i = 0; i < having.size(); i++) {
            EntryVersion version = having.get(i);
            versions.add(version(version, sense(version.getDocument(), key.toString()), null));
            boolean hasItNext = i + 1 < having.size()
                    && having.get(i + 1).getLanguage().equals(version.getLanguage())
                    && having.get(i + 1).getHeadword().equals(version.getHeadword())
                    && having.get(i + 1).getVersion() == version.getVersion() + 1;
            if (!hasItNext) {
                entryVersionRepository
                        .findByLanguageAndHeadwordAndVersion(
                                version.getLanguage(), version.getHeadword(), version.getVersion() + 1)
                        .ifPresent(dropping -> versions.add(version(dropping, null, "")));
            }
        }
        return new SenseHistoryResponse(key, learningItemRepository.countBySenseKey(key), versions);
    }

    private static SenseVersion version(EntryVersion version, Map<String, Object> sense, String retiredTowards) {
        return new SenseVersion(
                version.getLanguage(),
                version.getHeadword(),
                version.getVersion(),
                version.getWriter(),
                version.getBuiltAt(),
                sense,
                retiredTowards);
    }

    /** The sense with the key in this version, with the lemma and part of speech of its lexeme; null if absent. */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> sense(Map<String, Object> document, String key) {
        if (!(document.get("lexemes") instanceof List<?> lexemes)) {
            return null;
        }
        for (Object lexeme : lexemes) {
            Map<String, Object> map = (Map<String, Object>) lexeme;
            if (!(map.get("senses") instanceof List<?> senses)) {
                continue;
            }
            for (Object sense : senses) {
                Map<String, Object> written = (Map<String, Object>) sense;
                if (key.equals(written.get("id"))) {
                    Map<String, Object> located = new LinkedHashMap<>();
                    located.put("lemma", map.get("lemma"));
                    located.put("pos", map.get("pos"));
                    located.putAll(written);
                    return located;
                }
            }
        }
        return null;
    }
}
