package com.almonium.learning.wanted.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.book.service.BookEmailService;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.catalog.service.CatalogSearchService;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.service.UnlockDates;
import com.almonium.learning.wanted.dto.request.WantRequest;
import com.almonium.learning.wanted.dto.response.ArrivedRow;
import com.almonium.learning.wanted.dto.response.WantReceipt;
import com.almonium.learning.wanted.dto.response.WantedQueueDto;
import com.almonium.learning.wanted.dto.response.WantedRow;
import com.almonium.learning.wanted.model.entity.Preparation;
import com.almonium.learning.wanted.model.entity.Want;
import com.almonium.learning.wanted.model.enums.WantStatus;
import com.almonium.learning.wanted.repository.WantRepository;
import com.almonium.learning.wanted.repository.WantRepository.WantGroup;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
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
 * Wanted books (W1–W4): one live queue for new books and new languages, ranked by distinct readers, ties by the first
 * want. Wanting is free and unmetered; it promises a place in a public order and nothing else. A work under copyright
 * where we make books cannot be wanted unless it frees up within five years, so it rarely appears. A published edition settles every want of its work in its
 * language, and each wanter hears once, in the app and by email.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class WantService {
    /** How many works the "Being prepared" card shows. */
    static final int BEING_PREPARED = 2;

    /** How long an arrival stays under "Recently arrived". */
    static final Duration RECENTLY = Duration.ofDays(14);

    private final WantRepository wantRepository;
    private final CatalogWorkRepository catalogWorkRepository;
    private final BookRepository bookRepository;
    private final NotificationService notificationService;
    private final BookEmailService bookEmailService;
    private final PreparationService preparationService;
    private final Clock clock;

    public WantReceipt want(User user, WantRequest request) {
        CatalogWork work = catalogWorkRepository
                .findById(request.catalogWorkId())
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));
        if (work.getProductionStatus() == RightsStatus.PROTECTED && !freesSoon(work)) {
            throw new ResourceConflictException("This one is under copyright, so we can’t offer it.");
        }
        if (onShelf(work, request.language())) {
            throw new ResourceConflictException("It’s already on the shelf in this language.");
        }
        Want want = wantRepository
                .findByUserIdAndCatalogWorkIdAndLanguage(user.getId(), work.getId(), request.language())
                .orElseGet(() -> {
                    Want fresh = new Want();
                    fresh.setUser(user);
                    fresh.setCatalogWork(work);
                    fresh.setLanguage(request.language());
                    return fresh;
                });
        if (want.getStatus() == null || !WantStatus.STANDING.contains(want.getStatus())) {
            // Wanting again after a decline or an earlier arrival is feedback too: it rejoins the queue.
            want.setStatus(WantStatus.OPEN);
            want.setDecidedBy(null);
            want.setDecidedAt(null);
            want.setArrivedBook(null);
            want.setArrivedAt(null);
        }
        wantRepository.saveAndFlush(want);
        List<WantGroup> open = ranked(wantRepository.groups(List.of(WantStatus.OPEN)));
        int rank = 0;
        long readers = 0;
        for (int i = 0; i < open.size(); i++) {
            WantGroup group = open.get(i);
            if (group.getCatalogWorkId().equals(work.getId()) && group.getLanguage() == request.language()) {
                rank = i + 1;
                readers = group.getReaders();
            }
        }
        return new WantReceipt(work.getId(), request.language(), rank, shown(readers));
    }

    /**
     * A work under copyright can still be wanted when its day is in sight (W6): the catalogue knows the exact year it
     * frees up where we make books and that is within five years, so the want will be filled on 1 January.
     */
    private boolean freesSoon(CatalogWork work) {
        Integer freeFrom = work.getProductionFreeFrom();
        int year = LocalDate.now(clock).getYear();
        return freeFrom != null && freeFrom > year && freeFrom <= year + UnlockDates.COUNTDOWN_YEARS;
    }

    /** Tapping "Wanted" again takes the want back, silently. */
    public void withdraw(User user, UUID catalogWorkId, Language language) {
        wantRepository
                .findByUserIdAndCatalogWorkIdAndLanguage(user.getId(), catalogWorkId, language)
                .filter(want -> WantStatus.STANDING.contains(want.getStatus()))
                .ifPresent(wantRepository::delete);
    }

    @Transactional(readOnly = true)
    public WantedQueueDto queue(User viewer, boolean mineOnly) {
        Set<String> mine = wantRepository.findByUserIdAndStatusIn(viewer.getId(), WantStatus.STANDING).stream()
                .map(want -> key(want.getCatalogWork().getId(), want.getLanguage()))
                .collect(Collectors.toSet());
        List<WantGroup> open = ranked(wantRepository.groups(List.of(WantStatus.OPEN)));
        List<WantGroup> preparing = ranked(wantRepository.groups(List.of(WantStatus.IN_PROGRESS)));
        Set<UUID> workIds = new HashSet<>();
        open.forEach(group -> workIds.add(group.getCatalogWorkId()));
        preparing.forEach(group -> workIds.add(group.getCatalogWorkId()));
        Map<UUID, CatalogWork> works = catalogWorkRepository.findAllById(workIds).stream()
                .collect(Collectors.toMap(CatalogWork::getId, Function.identity()));

        List<WantedRow> queue = new ArrayList<>();
        int rank = 0;
        for (WantGroup group : open) {
            CatalogWork work = works.get(group.getCatalogWorkId());
            if (work == null || work.getProductionStatus() == RightsStatus.PROTECTED) {
                continue;
            }
            rank++;
            WantedRow row = row(work, group, rank, mine);
            if (!mineOnly || row.wantedByMe()) {
                queue.add(row);
            }
        }
        List<WantedRow> beingPrepared = preparing.stream()
                .map(group -> Optional.ofNullable(works.get(group.getCatalogWorkId()))
                        .map(work -> row(work, group, 0, mine))
                        .orElse(null))
                .filter(Objects::nonNull)
                .limit(BEING_PREPARED)
                .toList();

        Map<String, ArrivedRow> arrived = new LinkedHashMap<>();
        for (Want want : wantRepository.arrivedSince(Instant.now(clock).minus(RECENTLY))) {
            Book book = want.getArrivedBook();
            if (book == null) {
                continue;
            }
            arrived.putIfAbsent(
                    key(want.getCatalogWork().getId(), want.getLanguage()),
                    new ArrivedRow(
                            want.getCatalogWork().getId(),
                            book.getTitle(),
                            book.getAuthor(),
                            book.getLanguage(),
                            book.getEditionSlug(),
                            book.getCefrLevel(),
                            book.getCoverUrl(),
                            want.getArrivedAt()));
        }
        int mineCount = (int) open.stream()
                .filter(group -> mine.contains(key(group.getCatalogWorkId(), group.getLanguage())))
                .count();
        return new WantedQueueDto(beingPrepared, queue, List.copyOf(arrived.values()), mineCount);
    }

    /**
     * A published edition settles every standing want of its work in its language (the work found through the
     * catalogue link on the edition's work slug). Each wanter hears once.
     */
    public void settlePublished(Book book) {
        if (book.getWorkSlug() == null) {
            return;
        }
        List<Want> settled = new ArrayList<>();
        for (CatalogWork work : catalogWorkRepository.findByWorkSlug(book.getWorkSlug())) {
            settled.addAll(wantRepository.findByCatalogWorkIdAndLanguageAndStatusIn(
                    work.getId(), book.getLanguage(), WantStatus.STANDING));
        }
        if (settled.isEmpty()) {
            return;
        }
        Instant now = Instant.now(clock);
        settled.forEach(want -> {
            want.setStatus(WantStatus.ARRIVED);
            want.setArrivedBook(book);
            want.setArrivedAt(now);
        });
        wantRepository.saveAll(settled);
        String path = "/books/" + book.getEditionSlug();
        Map<UUID, Want> byUser = new LinkedHashMap<>();
        settled.forEach(want -> byUser.putIfAbsent(want.getUser().getId(), want));
        for (Want want : byUser.values()) {
            String month = BookEmailService.monthOf(want.getCreatedAt());
            notificationService.notifyOfRequestPublished(
                    want.getUser(), want.getId(), book.getTitle(), month, book.getCoverUrl(), path);
            bookEmailService.requestPublished(want.getUser(), book.getTitle(), month, path);
        }
    }

    /** Rank: distinct readers, most first; ties by whoever wanted first. */
    static List<WantGroup> ranked(List<WantGroup> groups) {
        return groups.stream()
                .sorted(Comparator.comparingLong(WantGroup::getReaders)
                        .reversed()
                        .thenComparing(WantGroup::getFirstWantedAt))
                .toList();
    }

    static Integer shown(long readers) {
        return readers >= CatalogSearchService.SHOWN_READERS ? Math.toIntExact(readers) : null;
    }

    private WantedRow row(CatalogWork work, WantGroup group, int rank, Set<String> mine) {
        String workSlug = work.getWorkSlug() != null
                        && !bookRepository.findByWorkSlug(work.getWorkSlug()).isEmpty()
                ? work.getWorkSlug()
                : null;
        return new WantedRow(
                work.getId(),
                work.getTitle(),
                work.getAuthorDisplay(),
                group.getLanguage(),
                workSlug,
                rank,
                shown(group.getReaders()),
                mine.contains(key(work.getId(), group.getLanguage())),
                group.getStatus(),
                group.getStatus() == WantStatus.IN_PROGRESS
                        ? preparationService
                                .find(work.getId(), group.getLanguage())
                                .map(Preparation::getProgress)
                                .orElse(null)
                        : null);
    }

    private boolean onShelf(CatalogWork work, Language language) {
        return work.getWorkSlug() != null
                && bookRepository.findByWorkSlug(work.getWorkSlug()).stream()
                        .anyMatch(book -> book.getLanguage() == language);
    }

    private static String key(UUID workId, Language language) {
        return workId + "|" + language;
    }
}
