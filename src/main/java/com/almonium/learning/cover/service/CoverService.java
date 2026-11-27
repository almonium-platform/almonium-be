package com.almonium.learning.cover.service;

import com.almonium.config.properties.CoverProperties;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.cover.client.CommonsClient;
import com.almonium.learning.cover.client.CommonsClient.CommonsFile;
import com.almonium.learning.cover.client.CoverJudge;
import com.almonium.learning.cover.dto.CoverArt;
import com.almonium.learning.cover.dto.CoverRow;
import com.almonium.learning.cover.model.CoverStatus;
import com.almonium.learning.cover.model.WorkCover;
import com.almonium.learning.cover.repository.WorkCoverRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Finds cover art for the works on the shelf with nobody watching. For one work: the files Wikimedia holds for it,
 * narrowed by the licence rule, shown to a vision model, and the one it picks kept as our own copy beside the record
 * of where it came from. A work with no good candidate gets a row saying so and keeps its typographic cover.
 *
 * <p>No class-level transaction: a work holds HTTP calls for seconds, and each row is saved on its own.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CoverService {
    private static final Set<String> RASTER = Set.of("image/jpeg", "image/png");
    private static final int MIN_WIDTH = 500;
    private static final int MAX_BYTES = 2_000_000;

    private final WorkCoverRepository coverRepository;
    private final BookRepository bookRepository;
    private final CatalogWorkRepository catalogWorkRepository;
    private final CommonsClient commons;
    private final CoverJudge judge;
    private final CoverProperties properties;
    private final Clock clock;

    public List<CoverArt> approved() {
        return coverRepository.approved();
    }

    public List<CoverRow> rows() {
        return coverRepository.rows();
    }

    public Optional<WorkCover> image(String workSlug) {
        return coverRepository
                .findById(workSlug)
                .filter(cover -> cover.getStatus() == CoverStatus.APPROVED && cover.getImage() != null);
    }

    /** Every work on the shelf that has never been looked at. Returns how many were. */
    public int prepareMissing() {
        if (!properties.enabled()) {
            return 0;
        }
        Set<String> seen = new HashSet<>(coverRepository.workSlugs());
        int prepared = 0;
        for (String workSlug : bookRepository.findWorkSlugs()) {
            if (seen.contains(workSlug)) {
                continue;
            }
            try {
                prepare(workSlug);
                prepared++;
            } catch (RuntimeException e) {
                // One work's failure (a timeout, a malformed answer) must not stop the rest; it is tried again later.
                log.warn("Cover for {} could not be prepared: {}", workSlug, e.getMessage());
            }
        }
        return prepared;
    }

    /** Looks for one work's cover now, replacing whatever was decided before. */
    public CoverStatus prepare(String workSlug) {
        if (!properties.enabled()) {
            throw new IllegalStateException("Covers are off: no model key is configured");
        }
        List<Book> editions = bookRepository.findByWorkSlug(workSlug);
        if (editions.isEmpty()) {
            throw new EntityNotFoundException("Work not found");
        }
        Book book = editions.stream()
                .filter(edition -> edition.getOriginalBook() == null)
                .findFirst()
                .orElse(editions.getFirst());

        WorkCover cover = coverRepository.findById(workSlug).orElseGet(WorkCover::new);
        cover.setWorkSlug(workSlug);
        cover.setCheckedAt(Instant.now(clock));
        clear(cover);

        Optional<String> wikidataId = catalogWorkRepository.findByWorkSlug(workSlug).stream()
                .map(CatalogWork::getWikidataId)
                .filter(id -> id != null && !id.isBlank())
                .findFirst();
        if (wikidataId.isEmpty()) {
            return none(cover, "The work is not linked to a catalogue entry with a Wikidata id.");
        }

        List<CommonsFile> candidates = new ArrayList<>();
        List<String> bases = new ArrayList<>();
        for (CommonsFile file : commons.filesFor(wikidataId.get())) {
            if (!RASTER.contains(file.mime())
                    || file.width() < MIN_WIDTH
                    || file.imageUrl().isBlank()) {
                continue;
            }
            Optional<String> basis = file.copyrighted()
                    ? Optional.empty()
                    : CoverLicence.basis(file.hiddenCategories(), properties.getMinYearsSinceDeath());
            if (basis.isPresent() && candidates.size() < properties.getMaxCandidates()) {
                candidates.add(file);
                bases.add(basis.get());
            }
        }
        if (candidates.isEmpty()) {
            return none(cover, "No file on Commons passed the licence rule.");
        }

        List<CoverJudge.Shown> shown = new ArrayList<>();
        List<Integer> shownIndex = new ArrayList<>();
        for (int index = 0; index < candidates.size(); index++) {
            byte[] bytes = commons.download(candidates.get(index).imageUrl());
            if (bytes != null && bytes.length > 0 && bytes.length <= MAX_BYTES) {
                shown.add(new CoverJudge.Shown(
                        candidates.get(index).title(), candidates.get(index).mime(), bytes));
                shownIndex.add(index);
            }
        }
        if (shown.isEmpty()) {
            return none(cover, "No candidate could be downloaded.");
        }

        CoverJudge.Verdict verdict = judge.choose(book.getTitle(), book.getAuthor(), shown);
        cover.setModel(verdict.model());
        cover.setReason(verdict.reason());
        if (verdict.pick() < 0) {
            cover.setStatus(CoverStatus.NONE);
            coverRepository.save(cover);
            return CoverStatus.NONE;
        }
        CommonsFile chosen = candidates.get(shownIndex.get(verdict.pick()));
        cover.setStatus(CoverStatus.APPROVED);
        cover.setImage(shown.get(verdict.pick()).bytes());
        cover.setContentType(chosen.mime());
        cover.setFocusX(verdict.focusX());
        cover.setFocusY(verdict.focusY());
        cover.setSourcePageUrl(chosen.pageUrl());
        cover.setArtist(chosen.artist());
        cover.setLicence(chosen.licence());
        cover.setLicenceBasis(bases.get(shownIndex.get(verdict.pick())));
        coverRepository.save(cover);
        log.info("Cover for {} taken from {}", workSlug, chosen.pageUrl());
        return CoverStatus.APPROVED;
    }

    /** An operator takes a cover down. The record of what it was stays; the image goes. */
    public void reject(String workSlug) {
        WorkCover cover =
                coverRepository.findById(workSlug).orElseThrow(() -> new EntityNotFoundException("Cover not found"));
        cover.setStatus(CoverStatus.REJECTED);
        cover.setImage(null);
        coverRepository.save(cover);
    }

    private CoverStatus none(WorkCover cover, String reason) {
        cover.setStatus(CoverStatus.NONE);
        cover.setReason(reason);
        coverRepository.save(cover);
        return CoverStatus.NONE;
    }

    private static void clear(WorkCover cover) {
        cover.setImage(null);
        cover.setContentType(null);
        cover.setFocusX(50);
        cover.setFocusY(50);
        cover.setSourcePageUrl(null);
        cover.setArtist(null);
        cover.setLicence(null);
        cover.setLicenceBasis(null);
        cover.setReason(null);
        cover.setModel(null);
    }
}
