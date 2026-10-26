package com.almonium.analyzer.analyzer.service;

import com.almonium.analyzer.analyzer.repository.DiscoverLookupLog;
import com.almonium.analyzer.analyzer.repository.DiscoverLookupLog.AnsweredBy;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.service.CurrentMember;
import com.almonium.learning.catalog.service.SearchRequester;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Writes one Discover lookup to the log the /ops languages pulse reads. A failed write is logged and dropped: the
 * visitor's word sheet never waits on, or fails because of, the bookkeeping.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DiscoverLookupRecorder {
    private final DiscoverLookupLog lookupLog;
    private final CurrentMember currentMember;
    private final SearchRequester searchRequester;
    private final Clock clock;

    public void record(String entry, Language language, Language translationLanguage, AnsweredBy answeredBy) {
        try {
            Optional<UUID> member = currentMember.id();
            lookupLog.log(
                    entry,
                    language,
                    translationLanguage,
                    answeredBy,
                    member.orElse(null),
                    searchRequester.of(member.orElse(null)),
                    Instant.now(clock));
        } catch (RuntimeException exception) {
            log.warn("Discover lookup not logged: {}", exception.getMessage());
        }
    }
}
