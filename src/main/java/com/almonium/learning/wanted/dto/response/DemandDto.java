package com.almonium.learning.wanted.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.wanted.model.enums.WantStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * /ops → Demand (W5), over the last {@code days} days: what readers want, what they searched for that is under
 * copyright (who might be worth a licensing conversation), and what they searched for that matched nothing.
 */
public record DemandDto(int days, List<Wanted> wanted, List<UnderCopyright> underCopyright, List<NoMatch> noMatch) {

    /** A wanted work in a language, by distinct readers; Start sends editorial to the catalogue with it prefilled. */
    public record Wanted(
            UUID catalogWorkId,
            String title,
            String author,
            Integer year,
            Language language,
            String workSlug,
            int readers,
            WantStatus status,
            Instant firstWantedAt,
            Instant lastWantedAt,
            String editorialUrl) {}

    /** A work under copyright readers looked for: how often, by how many, and its publisher where Wikidata has one. */
    public record UnderCopyright(
            UUID catalogWorkId,
            String title,
            String author,
            Integer year,
            String originalLanguage,
            int searches,
            int readers,
            String publisher,
            Integer freeFrom) {}

    /** A query that found nothing, with how often and by how many; "Link to work" teaches search an alias. */
    public record NoMatch(String query, int searches, int readers) {}
}
