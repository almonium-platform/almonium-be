package com.almonium.learning.wanted.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.response.ProcessorIngestStatus;
import com.almonium.learning.catalog.dto.response.TextSourceView;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.catalog.service.TextSources;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.wanted.dto.request.FactoryReservationRequest;
import com.almonium.learning.wanted.dto.response.FactoryQueueRow;
import com.almonium.learning.wanted.dto.response.FactoryReservation;
import com.almonium.learning.wanted.model.entity.Preparation;
import com.almonium.learning.wanted.model.entity.Want;
import com.almonium.learning.wanted.model.enums.WantStatus;
import com.almonium.learning.wanted.repository.WantRepository;
import com.almonium.learning.wanted.repository.WantRepository.WantGroup;
import com.almonium.user.core.exception.ResourceConflictException;
import java.time.Clock;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * What the factory on the host that makes the books asks of this deployment: the wanted works nobody is preparing,
 * ranked as /ops Demand ranks them and each with the free text to start from; a reservation, which is the same as
 * editorial pressing Start without a source request to the processor; and the progress it pushes afterwards. The
 * host itself decides when it has room, from its plan accounts' meters.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FactoryService {
    static final int QUEUE_LENGTH = 20;

    private final WantRepository wantRepository;
    private final CatalogWorkRepository catalogWorkRepository;
    private final TextSources textSources;
    private final PreparationService preparationService;
    private final Clock clock;

    @Transactional(readOnly = true)
    public List<FactoryQueueRow> queue() {
        List<WantGroup> groups = wantRepository.groups(List.of(WantStatus.OPEN)).stream()
                .sorted(Comparator.comparingLong(WantGroup::getReaders)
                        .reversed()
                        .thenComparing(WantGroup::getFirstWantedAt))
                .toList();
        List<UUID> workIds =
                groups.stream().map(WantGroup::getCatalogWorkId).distinct().toList();
        Map<UUID, CatalogWork> works = catalogWorkRepository.findAllById(workIds).stream()
                .collect(Collectors.toMap(CatalogWork::getId, Function.identity()));
        Set<String> prepared = preparationService.forWorks(workIds).stream()
                .map(preparation -> key(preparation.getCatalogWork().getId(), preparation.getLanguage()))
                .collect(Collectors.toSet());
        return groups.stream()
                .filter(group -> works.containsKey(group.getCatalogWorkId()))
                .filter(group -> !prepared.contains(key(group.getCatalogWorkId(), group.getLanguage())))
                .map(group -> row(works.get(group.getCatalogWorkId()), group))
                .limit(QUEUE_LENGTH)
                .toList();
    }

    public FactoryReservation reserve(FactoryReservationRequest request) {
        Preparation preparation =
                preparationService.reserve(request.catalogWorkId(), request.language(), request.sourceId());
        List<Want> wants = wantRepository.findByCatalogWorkIdAndLanguageAndStatusIn(
                request.catalogWorkId(), request.language(), WantStatus.STANDING);
        if (wants.isEmpty()) {
            throw new ResourceConflictException("Nobody is waiting on this one any more.");
        }
        Instant now = Instant.now(clock);
        wants.forEach(want -> {
            want.setStatus(WantStatus.IN_PROGRESS);
            want.setDecidedBy(null);
            want.setDecidedAt(now);
        });
        wantRepository.saveAll(wants);
        return new FactoryReservation(preparation.getId());
    }

    public void report(UUID preparationId, ProcessorIngestStatus status) {
        preparationService.report(preparationId, status);
    }

    private FactoryQueueRow row(CatalogWork work, WantGroup group) {
        return new FactoryQueueRow(
                work.getId(),
                group.getLanguage().name(),
                work.getTitle(),
                work.getAuthorDisplay(),
                work.getPublicationYear(),
                work.getOriginalLanguage(),
                Math.toIntExact(group.getReaders()),
                bestSource(work, group.getLanguage()).orElse(null));
    }

    /**
     * A free text the processor reads: the one a waiting reader chose if any did (the first choice found wins for
     * everyone), else in the wanted language if there is one, else the work's own, else any.
     */
    private Optional<FactoryQueueRow.Source> bestSource(CatalogWork work, Language wanted) {
        List<TextSourceView> free = textSources.forWork(work.getId()).stream()
                .filter(source -> source.status() == RightsStatus.FREE)
                .filter(source -> PreparationService.language(source.language()).isPresent())
                .toList();
        Set<String> chosen =
                wantRepository
                        .findByCatalogWorkIdAndLanguageAndStatusIn(work.getId(), wanted, WantStatus.STANDING)
                        .stream()
                        .map(Want::getSourceDownloadUrl)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toCollection(LinkedHashSet::new));
        String wantedCode = wanted.name().toLowerCase(Locale.ROOT);
        return free.stream()
                .filter(source -> chosen.contains(source.downloadUrl()))
                .findFirst()
                .or(() -> free.stream()
                        .filter(source -> wantedCode.equals(source.language()))
                        .findFirst())
                .or(() -> free.stream()
                        .filter(source ->
                                source.language() != null && source.language().equals(work.getOriginalLanguage()))
                        .findFirst())
                .or(() -> free.stream().findFirst())
                .map(source -> new FactoryQueueRow.Source(
                        source.id(), source.kind(), source.language(), source.pageUrl(), source.downloadUrl()));
    }

    private static String key(UUID workId, Language language) {
        return workId + ":" + language;
    }
}
