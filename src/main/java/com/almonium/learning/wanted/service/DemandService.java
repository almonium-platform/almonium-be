package com.almonium.learning.wanted.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogStore;
import com.almonium.learning.catalog.repository.CatalogStore.LoggedSearch;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.catalog.service.TitleFolding;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.wanted.dto.response.DemandDto;
import com.almonium.learning.wanted.model.entity.Preparation;
import com.almonium.learning.wanted.model.entity.Want;
import com.almonium.learning.wanted.model.enums.WantStatus;
import com.almonium.learning.wanted.repository.WantRepository;
import com.almonium.learning.wanted.repository.WantRepository.WantGroup;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * /ops → Demand (W5): what readers want, what they looked for that is under copyright, and what found nothing.
 * Wants say what to prepare next; copyright searches say which publishers might be worth a licensing conversation;
 * empty searches say what the catalogue is missing, and "Link to work" teaches search the alias.
 */
@Service
@Transactional
public class DemandService {
    /** A query followed within this long by a longer one from the same searcher was them still typing. */
    static final Duration TYPING = Duration.ofSeconds(60);

    private final WantRepository wantRepository;
    private final CatalogWorkRepository catalogWorkRepository;
    private final CatalogStore catalogStore;
    private final PreparationService preparationService;
    private final Clock clock;
    private final String editorialUrl;

    public DemandService(
            WantRepository wantRepository,
            CatalogWorkRepository catalogWorkRepository,
            CatalogStore catalogStore,
            PreparationService preparationService,
            Clock clock,
            @Value("${app.books.editorial-url:${app.books.processor-url}}") String editorialUrl) {
        this.wantRepository = wantRepository;
        this.catalogWorkRepository = catalogWorkRepository;
        this.catalogStore = catalogStore;
        this.preparationService = preparationService;
        this.clock = clock;
        this.editorialUrl = editorialUrl;
    }

    @Transactional(readOnly = true)
    public DemandDto demand(int days) {
        Instant since = Instant.now(clock).minus(Duration.ofDays(days));
        List<LoggedSearch> searches = finished(catalogStore.searchesSince(since));
        return new DemandDto(days, wanted(), underCopyright(searches), noMatch(searches));
    }

    /**
     * Editorial took the row on: it moves to "Being prepared" for everyone who wants it. With a source, the book
     * processor is asked to fetch that free text and ingest it, and the Wanted page shows its stages.
     */
    public void start(User operator, UUID catalogWorkId, Language language, UUID sourceId) {
        if (sourceId != null) {
            preparationService.start(operator, catalogWorkId, language, sourceId);
        }
        decide(operator, catalogWorkId, language, List.of(WantStatus.OPEN), WantStatus.IN_PROGRESS);
    }

    private static DemandDto.Preparing preparing(Preparation preparation) {
        return new DemandDto.Preparing(
                preparation.getSourceKind(),
                preparation.getSourceReference(),
                preparation.getSourceLanguage(),
                preparation.getPhase(),
                preparation.getProgress() == null ? 0 : preparation.getProgress(),
                preparation.getError());
    }

    /** One click, no reason, no mail: the row leaves the queue. */
    public void decline(User operator, UUID catalogWorkId, Language language) {
        decide(operator, catalogWorkId, language, WantStatus.STANDING, WantStatus.DECLINED);
    }

    /** "Link to work": the query, folded, now finds the work first. */
    public void alias(User operator, String query, UUID catalogWorkId) {
        String folded = TitleFolding.fold(query);
        if (folded.isEmpty()) {
            throw new ResourceConflictException("The query has no letters or digits to match.");
        }
        catalogWorkRepository.findById(catalogWorkId).orElseThrow(() -> new EntityNotFoundException("Work not found"));
        catalogStore.saveAlias(folded, catalogWorkId, operator == null ? null : operator.getId(), Instant.now(clock));
    }

    private void decide(User operator, UUID catalogWorkId, Language language, List<WantStatus> from, WantStatus to) {
        List<Want> wants = wantRepository.findByCatalogWorkIdAndLanguageAndStatusIn(catalogWorkId, language, from);
        if (wants.isEmpty()) {
            throw new ResourceConflictException("Nobody is waiting on this one any more.");
        }
        Instant now = Instant.now(clock);
        wants.forEach(want -> {
            want.setStatus(to);
            want.setDecidedBy(operator);
            want.setDecidedAt(now);
        });
        wantRepository.saveAll(wants);
    }

    private List<DemandDto.Wanted> wanted() {
        List<WantGroup> groups = wantRepository.groups(WantStatus.STANDING);
        Map<UUID, CatalogWork> works =
                works(groups.stream().map(WantGroup::getCatalogWorkId).toList());
        return groups.stream()
                .sorted(Comparator.comparing((WantGroup group) -> group.getStatus() != WantStatus.IN_PROGRESS)
                        .thenComparing(
                                Comparator.comparingLong(WantGroup::getReaders).reversed())
                        .thenComparing(WantGroup::getFirstWantedAt))
                .filter(group -> works.containsKey(group.getCatalogWorkId()))
                .map(group -> {
                    CatalogWork work = works.get(group.getCatalogWorkId());
                    return new DemandDto.Wanted(
                            work.getId(),
                            work.getTitle(),
                            work.getAuthorDisplay(),
                            work.getPublicationYear(),
                            group.getLanguage(),
                            work.getWorkSlug(),
                            Math.toIntExact(group.getReaders()),
                            group.getStatus(),
                            group.getFirstWantedAt(),
                            group.getLastWantedAt(),
                            editorialUrl(work, group.getLanguage()),
                            preparationService
                                    .find(work.getId(), group.getLanguage())
                                    .map(DemandService::preparing)
                                    .orElse(null));
                })
                .toList();
    }

    private List<DemandDto.UnderCopyright> underCopyright(List<LoggedSearch> searches) {
        Map<UUID, List<LoggedSearch>> byWork = searches.stream()
                .filter(search -> RightsStatus.PROTECTED.name().equals(search.topState()) && search.topWorkId() != null)
                .collect(Collectors.groupingBy(LoggedSearch::topWorkId));
        Map<UUID, CatalogWork> works = works(byWork.keySet());
        return byWork.entrySet().stream()
                .filter(entry -> works.containsKey(entry.getKey()))
                .map(entry -> {
                    CatalogWork work = works.get(entry.getKey());
                    return new DemandDto.UnderCopyright(
                            work.getId(),
                            work.getTitle(),
                            work.getAuthorDisplay(),
                            work.getPublicationYear(),
                            work.getOriginalLanguage(),
                            entry.getValue().size(),
                            readers(entry.getValue()),
                            work.getPublisher(),
                            work.getProductionFreeFrom());
                })
                .sorted(Comparator.comparingInt(DemandDto.UnderCopyright::readers)
                        .reversed()
                        .thenComparing(Comparator.comparingInt(DemandDto.UnderCopyright::searches)
                                .reversed()))
                .toList();
    }

    private List<DemandDto.NoMatch> noMatch(List<LoggedSearch> searches) {
        return searches.stream()
                .filter(search -> search.topWorkId() == null)
                .collect(Collectors.groupingBy(LoggedSearch::query))
                .entrySet()
                .stream()
                .map(entry ->
                        new DemandDto.NoMatch(entry.getKey(), entry.getValue().size(), readers(entry.getValue())))
                .sorted(Comparator.comparingInt(DemandDto.NoMatch::readers)
                        .reversed()
                        .thenComparing(Comparator.comparingInt(DemandDto.NoMatch::searches)
                                .reversed()))
                .toList();
    }

    /**
     * Search runs as the reader types, so "sherl", "sherlo", "sherlock" from one person is one search for
     * "sherlock". A query is dropped when the same searcher's next query, within a minute, starts with it.
     * The log arrives ordered by searcher, then time.
     */
    static List<LoggedSearch> finished(List<LoggedSearch> log) {
        List<LoggedSearch> kept = new ArrayList<>();
        for (int i = 0; i < log.size(); i++) {
            LoggedSearch current = log.get(i);
            LoggedSearch next = i + 1 < log.size() ? log.get(i + 1) : null;
            boolean stillTyping = next != null
                    && next.requester().equals(current.requester())
                    && !next.at().isAfter(current.at().plus(TYPING))
                    && next.query().startsWith(current.query());
            if (!stillTyping) {
                kept.add(current);
            }
        }
        return kept;
    }

    private static int readers(List<LoggedSearch> searches) {
        Set<String> requesters = new HashSet<>();
        searches.forEach(search -> requesters.add(search.requester()));
        return requesters.size();
    }

    private Map<UUID, CatalogWork> works(Collection<UUID> ids) {
        return catalogWorkRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(CatalogWork::getId, Function.identity(), (a, b) -> a, HashMap::new));
    }

    private String editorialUrl(CatalogWork work, Language language) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(editorialUrl)
                .queryParam("title", work.getTitle())
                .queryParam("author", work.getAuthorDisplay() == null ? "" : work.getAuthorDisplay())
                .queryParam("language", language.name().toLowerCase(Locale.ROOT));
        if (work.getWorkSlug() != null) {
            builder.queryParam("work", work.getWorkSlug());
        }
        if (work.getGutenbergId() != null) {
            builder.queryParam("gutenberg", work.getGutenbergId());
        }
        return builder.encode(StandardCharsets.UTF_8).build().toUriString();
    }
}
