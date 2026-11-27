package com.almonium.learning.catalog.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.service.CurrentMember;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.dto.response.CatalogHit;
import com.almonium.learning.catalog.dto.response.CatalogHit.CatalogState;
import com.almonium.learning.catalog.dto.response.WorkUnlocksView;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogStore;
import com.almonium.learning.catalog.repository.CatalogStore.Hit;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.rights.engine.CountryTerms;
import com.almonium.learning.rights.model.RightsFacts;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.service.ReaderLocation;
import com.almonium.learning.rights.service.ReadingRights;
import com.almonium.learning.rights.service.UnlockDates;
import com.almonium.learning.wanted.repository.WantRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    /** Countries named in "already free in". */
    static final int FREE_IN_COUNTRIES = 3;

    /**
     * The largest series a search folds. Wikidata files 350 Doctor Who books and the whole Sherlock Holmes canon (62)
     * as series; unfolding those would bury every other hit.
     */
    static final int MAX_FOLDED_SERIES = 30;

    private final CatalogStore store;
    private final CatalogWorkRepository catalogWorkRepository;
    private final BookRepository bookRepository;
    private final ReadingRights readingRights;
    private final ReaderLocation readerLocation;
    private final UnlockDates unlockDates;
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
        Map<UUID, CatalogHit.CatalogSeries> series = new HashMap<>();
        List<Hit> shown = foldSeries(hits, series);
        return decorate(shown, language, member, loadRightsFacts(shown), series);
    }

    /**
     * Where two or more hits belong to one series of at most {@link #MAX_FOLDED_SERIES} works, the whole series takes
     * the place of its best hit, in reading order, and the rest of its hits drop out. Fills {@code series} for every
     * work so placed.
     */
    List<Hit> foldSeries(List<Hit> hits, Map<UUID, CatalogHit.CatalogSeries> series) {
        Map<UUID, CatalogStore.SeriesRef> memberships =
                store.seriesOf(hits.stream().map(Hit::id).toList());
        Map<String, Long> counts = memberships.values().stream()
                .collect(Collectors.groupingBy(CatalogStore.SeriesRef::id, Collectors.counting()));
        Set<String> repeated = counts.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        Map<String, Integer> sizes = store.seriesSizes(repeated);
        Set<String> foldable = repeated.stream()
                .filter(id -> sizes.getOrDefault(id, 0) <= MAX_FOLDED_SERIES)
                .collect(Collectors.toSet());
        if (foldable.isEmpty()) {
            return hits;
        }
        Map<UUID, Hit> placed = new LinkedHashMap<>();
        for (Hit hit : hits) {
            CatalogStore.SeriesRef ref = memberships.get(hit.id());
            if (ref == null || !foldable.contains(ref.id())) {
                placed.putIfAbsent(hit.id(), hit);
            } else if (series.values().stream().noneMatch(s -> s.id().equals(ref.id()))) {
                List<Hit> members = store.seriesMembers(ref.id());
                Map<UUID, CatalogStore.SeriesRef> places =
                        store.seriesOf(members.stream().map(Hit::id).toList());
                for (Hit work : members) {
                    placed.remove(work.id());
                    placed.put(work.id(), work);
                    CatalogStore.SeriesRef place = places.get(work.id());
                    series.put(
                            work.id(),
                            new CatalogHit.CatalogSeries(
                                    ref.id(), ref.title(), place == null ? null : place.position(), members.size()));
                }
            }
        }
        return List.copyOf(placed.values());
    }

    /**
     * Public Domain Day: the best-known works that become free to read in the reader's country on 1 January of
     * {@code year} (free both where we make books and in the reader's country, computed dynamically by the copyright
     * engine). Not logged: nobody searched.
     */
    @Transactional(readOnly = true)
    public List<CatalogHit> freeFrom(int year, int limit, Language language) {
        String effectiveCountry = readerLocation.country();
        CountryTerms.CandidateWindow window = CountryTerms.candidateWindow(effectiveCountry, year);
        int candidateLimit = Math.max(limit * 10, 200);
        List<Hit> candidates = store.freeFromCandidates(year, window, candidateLimit);
        if (candidates.isEmpty()) {
            return List.of();
        }
        Map<UUID, RightsFacts> factsById = loadRightsFacts(candidates);
        List<Hit> matched = candidates.stream()
                .filter(hit -> {
                    RightsFacts facts = factsById.get(hit.id());
                    return facts != null && Objects.equals(unlockDates.readerFreeFrom(facts, effectiveCountry), year);
                })
                .limit(limit)
                .toList();
        return decorate(matched, language, currentMember.id(), factsById);
    }

    /**
     * One work as its own page shows it (W15): the answer a search would give for it. A visit counts as demand (W5),
     * so it is logged the way a search that found the work is.
     */
    public CatalogHit work(UUID catalogWorkId, Language language) {
        CatalogWork work = catalogWorkRepository
                .findById(catalogWorkId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));
        Hit hit = new Hit(
                work.getId(),
                work.getTitle(),
                work.getOriginalTitle(),
                work.getOriginalLanguage(),
                work.getPublicationYear(),
                work.getAuthorDisplay(),
                work.getPopularity(),
                work.getWorkSlug(),
                work.getProductionStatus(),
                work.getProductionFreeFrom(),
                1);
        Optional<UUID> member = currentMember.id();
        store.logSearch(
                TitleFolding.fold(work.getTitle()),
                hit,
                member.orElse(null),
                searchRequester.of(member.orElse(null)),
                Instant.now(clock));
        return decorate(List.of(hit), language, member).getFirst();
    }

    /** Where and when one work is free to read, country by country (W7). */
    @Transactional(readOnly = true)
    public WorkUnlocksView unlocks(UUID catalogWorkId) {
        CatalogWork work = catalogWorkRepository
                .findById(catalogWorkId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));
        RightsFacts facts = work.rightsFacts();
        String effectiveCountry = readerLocation.country();
        String detectedCountry = readerLocation.detectedCountry().orElse(null);
        return new WorkUnlocksView(
                work.getId(),
                work.getTitle(),
                work.getAuthorDisplay(),
                work.getPublicationYear(),
                detectedCountry,
                unlockDates.readerFreeFrom(facts, effectiveCountry),
                unlockDates.everywhere(facts));
    }

    public int currentYear() {
        return LocalDate.now(clock).getYear();
    }

    /** Each hit with what the reader can do, and, in a language, how many want it and whether the reader does. */
    private List<CatalogHit> decorate(List<Hit> hits, Language language, Optional<UUID> member) {
        return decorate(hits, language, member, loadRightsFacts(hits));
    }

    private List<CatalogHit> decorate(
            List<Hit> hits, Language language, Optional<UUID> member, Map<UUID, RightsFacts> facts) {
        return decorate(hits, language, member, facts, Map.of());
    }

    private List<CatalogHit> decorate(
            List<Hit> hits,
            Language language,
            Optional<UUID> member,
            Map<UUID, RightsFacts> facts,
            Map<UUID, CatalogHit.CatalogSeries> series) {
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
        String effectiveCountry = readerLocation.country();
        String detectedCountry = readerLocation.detectedCountry().orElse(null);
        return hits.stream()
                .map(hit -> toHit(
                        hit,
                        counts.getOrDefault(hit.id(), 0L),
                        wanted.contains(hit.id()),
                        effectiveCountry,
                        detectedCountry,
                        facts.get(hit.id()),
                        series.get(hit.id())))
                .toList();
    }

    private Map<UUID, RightsFacts> loadRightsFacts(List<Hit> hits) {
        return hits.isEmpty()
                ? Map.of()
                : catalogWorkRepository.findAllById(hits.stream().map(Hit::id).toList()).stream()
                        .collect(Collectors.toMap(CatalogWork::getId, CatalogWork::rightsFacts));
    }

    private CatalogHit toHit(
            Hit hit,
            long readers,
            boolean wantedByMe,
            String effectiveCountry,
            String detectedCountry,
            RightsFacts facts,
            CatalogHit.CatalogSeries series) {
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
                wantedByMe,
                detectedCountry,
                facts == null ? null : unlockDates.readerFreeFrom(facts, effectiveCountry),
                facts == null || state != CatalogState.UNDER_COPYRIGHT
                        ? List.of()
                        : unlockDates.alreadyFreeIn(facts, effectiveCountry, FREE_IN_COUNTRIES),
                series);
    }
}
