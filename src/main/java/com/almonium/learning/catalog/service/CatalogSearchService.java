package com.almonium.learning.catalog.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.service.CurrentMember;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.dto.response.CatalogHit;
import com.almonium.learning.catalog.dto.response.CatalogHit.CatalogState;
import com.almonium.learning.catalog.repository.CatalogStore;
import com.almonium.learning.catalog.repository.CatalogStore.Hit;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.service.ReadingRights;
import com.almonium.learning.wanted.repository.WantRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Search over every notable book (W1): a query always lands on real works, each marked by what the reader can do with
 * it. A work counts as on the shelf only while an edition of it is published, and only for a country that may read it.
 * Every search is logged with its top hit for the /ops demand page (W5), including the ones that find nothing.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CatalogSearchService {
    static final int MIN_QUERY = 2;

    /** Below this many readers a count is not shown, so a lone want never reads as "1 reader". */
    public static final int SHOWN_READERS = 3;

    private final CatalogStore store;
    private final BookRepository bookRepository;
    private final ReadingRights readingRights;
    private final WantRepository wantRepository;
    private final CurrentMember currentMember;
    private final SearchRequester searchRequester;
    private final Clock clock;

    public List<CatalogHit> search(String query, int limit) {
        return search(query, limit, null);
    }

    /** {@code language}, when given, is the language the reader would read in: want counts are for it. */
    public List<CatalogHit> search(String query, int limit, Language language) {
        String folded = TitleFolding.fold(query);
        if (folded.length() < MIN_QUERY) {
            return List.of();
        }
        List<Hit> hits = store.search(folded, limit);
        Optional<UUID> member = currentMember.id();
        store.logSearch(
                folded,
                hits.isEmpty() ? null : hits.getFirst(),
                member.orElse(null),
                searchRequester.of(member.orElse(null)),
                Instant.now(clock));
        return decorate(hits, language, member);
    }

    /**
     * Public Domain Day: the best-known works that become free on 1 January of {@code year} where we make books, or,
     * for the current year, the ones that became free this January. Not logged: nobody searched.
     */
    @Transactional(readOnly = true)
    public List<CatalogHit> freeFrom(int year, int limit, Language language) {
        return decorate(store.freeFrom(year, limit), language, currentMember.id());
    }

    public int currentYear() {
        return LocalDate.now(clock).getYear();
    }

    /** Each hit with what the reader can do, and, in a language, how many want it and whether the reader does. */
    private List<CatalogHit> decorate(List<Hit> hits, Language language, Optional<UUID> member) {
        Map<UUID, Long> readers = Map.of();
        Set<UUID> mine = Set.of();
        if (language != null && !hits.isEmpty()) {
            List<UUID> ids = hits.stream().map(Hit::id).toList();
            readers = wantRepository.readers(ids, language).stream()
                    .collect(Collectors.toMap(
                            WantRepository.WorkReaders::getCatalogWorkId, WantRepository.WorkReaders::getReaders));
            mine = member.<Set<UUID>>map(id -> new HashSet<>(wantRepository.wantedBy(id, ids, language)))
                    .orElse(Set.of());
        }
        Map<UUID, Long> counts = readers;
        Set<UUID> wanted = mine;
        return hits.stream()
                .map(hit -> toHit(hit, counts.getOrDefault(hit.id(), 0L), wanted.contains(hit.id())))
                .toList();
    }

    private CatalogHit toHit(Hit hit, long readers, boolean wantedByMe) {
        CatalogState state;
        if (hit.workSlug() != null
                && !bookRepository.findByWorkSlug(hit.workSlug()).isEmpty()) {
            state = readingRights.mayRead(hit.workSlug()) ? CatalogState.ON_SHELF : CatalogState.NOT_IN_YOUR_COUNTRY;
        } else if (hit.productionStatus() == RightsStatus.PROTECTED) {
            state = CatalogState.UNDER_COPYRIGHT;
        } else {
            state = CatalogState.WANTABLE;
        }
        return new CatalogHit(
                hit.id(),
                hit.title(),
                hit.originalTitle(),
                hit.originalLanguage(),
                hit.authorDisplay(),
                hit.publicationYear(),
                state,
                hit.workSlug(),
                hit.productionFreeFrom(),
                readers >= SHOWN_READERS ? Math.toIntExact(readers) : null,
                wantedByMe);
    }
}
