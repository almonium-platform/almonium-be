package com.almonium.learning.rights.service;

import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogStore;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.rights.dto.request.RightsOverrideRequest;
import com.almonium.learning.rights.dto.response.RightsOverrideRow;
import com.almonium.learning.rights.dto.response.RightsReportRow;
import com.almonium.learning.rights.dto.response.WorkRightsRow;
import com.almonium.learning.rights.dto.response.WorkRightsView;
import com.almonium.learning.rights.engine.CopyrightEngine;
import com.almonium.learning.rights.engine.CountryTerms;
import com.almonium.learning.rights.model.CountryVerdict;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.model.entity.RightsOverride;
import com.almonium.learning.rights.model.entity.RightsReport;
import com.almonium.learning.rights.model.entity.WorkRightsBlock;
import com.almonium.learning.rights.model.enums.RightsReportStatus;
import com.almonium.learning.rights.repository.RightsOverrideRepository;
import com.almonium.learning.rights.repository.RightsReportRepository;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * /ops → Rights: the report queue, takedowns and allowances per country, and the facts behind every shelf work's
 * verdict. Every change recomputes the work's block list at once; other application slots pick it up within a minute.
 */
@Service
@RequiredArgsConstructor
public class RightsOperationsService {
    private final RightsReportRepository reportRepository;
    private final RightsOverrideRepository overrideRepository;
    private final WorkRightsService workRightsService;
    private final ProductionRights productionRights;
    private final CopyrightEngine engine;
    private final BookRepository bookRepository;
    private final CatalogWorkRepository catalogWorkRepository;
    private final CatalogStore catalogStore;
    private final Clock clock;

    @Transactional(readOnly = true)
    public List<RightsReportRow> reports() {
        List<RightsReport> reports =
                new ArrayList<>(reportRepository.findByStatusOrderByCreatedAtDesc(RightsReportStatus.OPEN));
        reportRepository.findTop100ByOrderByCreatedAtDesc().stream()
                .filter(report -> report.getStatus() != RightsReportStatus.OPEN)
                .forEach(reports::add);
        return reports.stream().map(RightsOperationsService::toRow).toList();
    }

    @Transactional
    public RightsReportRow dismiss(User operator, UUID reportId) {
        RightsReport report = report(reportId);
        settle(report, operator, RightsReportStatus.DISMISSED);
        return toRow(report);
    }

    /** A takedown (BLOCK) or an allowance (ALLOW) for one work in one country, or everywhere with {@code *}. */
    @Transactional
    public RightsOverrideRow override(User operator, RightsOverrideRequest request) {
        String country = request.country().toUpperCase(Locale.ROOT);
        String workSlug = request.workSlug().trim();
        if (bookRepository.findByWorkSlug(workSlug).isEmpty()
                && catalogWorkRepository.findByWorkSlug(workSlug).isEmpty()) {
            throw new EntityNotFoundException("No shelf work with slug " + workSlug);
        }
        RightsOverride override =
                overrideRepository.findByWorkSlugAndCountry(workSlug, country).orElseGet(RightsOverride::new);
        override.setWorkSlug(workSlug);
        override.setCountry(country);
        override.setEffect(request.effect());
        override.setReason(request.reason().trim());
        override.setCreatedBy(operator);
        if (request.reportId() != null) {
            RightsReport report = report(request.reportId());
            override.setReport(report);
            settle(report, operator, RightsReportStatus.ACTIONED);
        }
        RightsOverride saved = overrideRepository.saveAndFlush(override);
        workRightsService.recompute(workSlug);
        return toRow(saved);
    }

    @Transactional
    public void removeOverride(UUID overrideId) {
        RightsOverride override = overrideRepository
                .findById(overrideId)
                .orElseThrow(() -> new EntityNotFoundException("Override not found"));
        overrideRepository.delete(override);
        overrideRepository.flush();
        workRightsService.recompute(override.getWorkSlug());
    }

    /** Every shelf work, the ones hidden everywhere and the unlinked ones (thin facts) first. */
    @Transactional(readOnly = true)
    public List<WorkRightsRow> works() {
        Map<String, Book> representatives = representatives();
        List<WorkRightsRow> rows = new ArrayList<>();
        representatives.forEach((slug, book) -> {
            ShelfWorkFacts facts = workRightsService.factsFor(slug);
            var blocked = workRightsService.blockedCountries(slug);
            rows.add(new WorkRightsRow(
                    slug,
                    book.getTitle(),
                    book.getAuthor(),
                    facts.linked(),
                    productionRights.verdict(facts.facts()).status(),
                    blocked.contains(WorkRightsBlock.EVERYWHERE),
                    (int) blocked.stream()
                            .filter(country -> !WorkRightsBlock.EVERYWHERE.equals(country))
                            .count(),
                    overrideRepository.findByWorkSlug(slug).size()));
        });
        rows.sort(Comparator.comparing((WorkRightsRow row) -> !row.blockedEverywhere())
                .thenComparing(WorkRightsRow::linked)
                .thenComparing(WorkRightsRow::title, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));
        return rows;
    }

    @Transactional(readOnly = true)
    public WorkRightsView work(String workSlug) {
        Book book = representatives().get(workSlug);
        ShelfWorkFacts facts = workRightsService.factsFor(workSlug);
        if (book == null && !facts.linked()) {
            throw new EntityNotFoundException("No shelf work with slug " + workSlug);
        }
        int year = productionRights.currentYear();
        CountryVerdict production = productionRights.verdict(facts.facts(), year);
        Map<String, Integer> protectedUntil = new TreeMap<>();
        List<String> unknown = new ArrayList<>();
        for (String country : CountryTerms.countries()) {
            CountryVerdict verdict = engine.verdict(facts.facts(), country, year);
            if (verdict.status() == RightsStatus.PROTECTED) {
                protectedUntil.put(country, verdict.shownYear());
            } else if (verdict.status() == RightsStatus.UNKNOWN) {
                unknown.add(country);
            }
        }
        Map<String, String> blocked = new TreeMap<>();
        workRightsService.computeBlocks(workSlug).forEach((country, source) -> blocked.put(country, source.name()));
        CatalogWork catalogWork = facts.catalogWork();
        return new WorkRightsView(
                workSlug,
                catalogWork != null ? catalogWork.getTitle() : book.getTitle(),
                catalogWork != null ? catalogWork.getAuthorDisplay() : book.getAuthor(),
                catalogWork != null ? catalogWork.getId() : null,
                catalogWork != null ? catalogWork.getWikidataId() : null,
                facts.facts().publicationYear(),
                catalogWork != null ? catalogWork.getAuthors() : List.of(),
                production.status(),
                production.shownYear(),
                blocked,
                protectedUntil,
                unknown.stream().sorted().toList(),
                overrideRepository.findByWorkSlug(workSlug).stream()
                        .map(RightsOperationsService::toRow)
                        .toList());
    }

    /** Points a shelf work at the catalogue work it is an edition of, replacing any earlier link. */
    @Transactional
    public WorkRightsView link(String workSlug, UUID catalogWorkId) {
        CatalogWork work = catalogWorkRepository
                .findById(catalogWorkId)
                .orElseThrow(() -> new EntityNotFoundException("Catalogue work not found"));
        if (work.getWorkSlug() != null && !work.getWorkSlug().equals(workSlug)) {
            throw new ResourceConflictException("That catalogue work is already linked to " + work.getWorkSlug());
        }
        catalogStore.unlinkWorkSlug(workSlug);
        catalogStore.linkWorkSlug(catalogWorkId, workSlug);
        workRightsService.recompute(workSlug);
        return work(workSlug);
    }

    public int recomputeAll() {
        return workRightsService.recomputeAll();
    }

    /** One edition per shelf work to name it by: the original where there is one. */
    private Map<String, Book> representatives() {
        Map<String, Book> byWork = new LinkedHashMap<>();
        bookRepository.findAll().stream()
                .filter(book -> book.getWorkSlug() != null)
                .sorted(Comparator.comparing((Book book) -> book.getOriginalBook() != null))
                .forEach(book -> byWork.putIfAbsent(book.getWorkSlug(), book));
        return byWork;
    }

    private RightsReport report(UUID id) {
        return reportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Report not found"));
    }

    private void settle(RightsReport report, User operator, RightsReportStatus status) {
        report.setStatus(status);
        report.setHandledBy(operator);
        report.setHandledAt(Instant.now(clock));
    }

    private static RightsReportRow toRow(RightsReport report) {
        Book book = report.getBook();
        return new RightsReportRow(
                report.getId(),
                report.getWorkSlug(),
                book == null ? null : book.getEditionSlug(),
                book == null ? null : book.getTitle(),
                report.getReporterRole(),
                report.getReporterName(),
                report.getReporterEmail(),
                report.getCountry(),
                report.getDetectedCountry(),
                report.getMessage(),
                report.getStatus(),
                report.getCreatedAt(),
                report.getHandledAt());
    }

    private static RightsOverrideRow toRow(RightsOverride override) {
        return new RightsOverrideRow(
                override.getId(),
                override.getWorkSlug(),
                override.getCountry(),
                override.getEffect(),
                override.getReason(),
                override.getReport() == null ? null : override.getReport().getId(),
                override.getCreatedAt());
    }
}
