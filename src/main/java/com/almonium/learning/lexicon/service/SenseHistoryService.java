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
 * stood there, and the version that retired it with the key it was retired towards. How many cards were saved from
 * the sense comes along, so an editor sees who a change would reach.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SenseHistoryService {
    private final EntryVersionRepository entryVersionRepository;
    private final LearningItemRepository learningItemRepository;

    public SenseHistoryResponse history(UUID key) {
        String live = "{\"lexemes\": [{\"senses\": [{\"key\": \"" + key + "\"}]}]}";
        List<SenseVersion> versions = new ArrayList<>();
        for (EntryVersion version : entryVersionRepository.findContaining(live, key.toString())) {
            Map<String, Object> document = version.getDocument();
            versions.add(new SenseVersion(
                    version.getLanguage(),
                    version.getHeadword(),
                    version.getVersion(),
                    version.getWriter(),
                    version.getBuiltAt(),
                    sense(document, key.toString()),
                    retiredTowards(document, key.toString())));
        }
        return new SenseHistoryResponse(key, learningItemRepository.countBySenseKey(key), versions);
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
                if (key.equals(written.get("key"))) {
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

    /** "retired" when this version dropped the sense: the key of its nearest successor, or the empty string. */
    private static String retiredTowards(Map<String, Object> document, String key) {
        if (document.get("retired") instanceof Map<?, ?> retired && retired.containsKey(key)) {
            Object nearest = retired.get(key);
            return nearest == null ? "" : nearest.toString();
        }
        return null;
    }
}
