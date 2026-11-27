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
import com.almonium.learning.cover.dto.CoverCandidate;
import com.almonium.learning.cover.dto.CoverCandidates;
import com.almonium.learning.cover.dto.CoverDecision;
import com.almonium.learning.cover.dto.CoverRow;
import com.almonium.learning.cover.model.CoverStatus;
import com.almonium.learning.cover.model.WorkCover;
import com.almonium.learning.cover.repository.WorkCoverRepository;
import jakarta.persistence.EntityNotFoundException;
import java.net.URI;
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
 * narrowed by the licence rule, shown to a judge, and the one it picks kept as our own copy beside the record of
 * where it came from. A work with no good candidate gets a row saying so and keeps its typographic cover. The judge
 * is a vision model on the owner's plan, asked by {@code scripts/covers/choose_covers.py} through the internal
 * endpoints, or the API's model asked from here when {@code covers.api-judge} is on.
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
    private static final int CENTRE = 50;
    /** Where Commons serves its files and their thumbnails from; a decision may name no other host. */
    private static final Set<String> COMMONS_HOSTS = Set.of("upload.wikimedia.org", "thumb.wikimedia.org");

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

    /** The works on the shelf that have never been looked at, in order. */
    public List<String> pending() {
        Set<String> seen = new HashSet<>(coverRepository.workSlugs());
        return bookRepository.findWorkSlugs().stream()
                .filter(workSlug -> !seen.contains(workSlug))
                .sorted()
                .toList();
    }

    /** Every pending work, judged from here. Returns how many were; nothing when the plan script is the judge. */
    public int prepareMissing() {
        if (!properties.judgesHere()) {
            return 0;
        }
        int prepared = 0;
        for (String workSlug : pending()) {
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

    /**
     * The files a judge may choose one work's cover from: what Wikimedia holds for it, narrowed by the licence rule.
     * The rule lives here and nowhere else, so a judge outside this service is never shown a file it may not take.
     */
    public CoverCandidates candidates(String workSlug) {
        List<Book> editions = bookRepository.findByWorkSlug(workSlug);
        if (editions.isEmpty()) {
            throw new EntityNotFoundException("Work not found");
        }
        Book book = editions.stream()
                .filter(edition -> edition.getOriginalBook() == null)
                .findFirst()
                .orElse(editions.getFirst());
        Optional<String> wikidataId = catalogWorkRepository.findByWorkSlug(workSlug).stream()
                .map(CatalogWork::getWikidataId)
                .filter(id -> id != null && !id.isBlank())
                .findFirst();
        if (wikidataId.isEmpty()) {
            return new CoverCandidates(
                    workSlug,
                    book.getTitle(),
                    book.getAuthor(),
                    List.of(),
                    "The work is not linked to a catalogue entry with a Wikidata id.");
        }
        List<CoverCandidate> candidates = new ArrayList<>();
        for (CommonsFile file : commons.filesFor(wikidataId.get())) {
            if (candidates.size() >= properties.getMaxCandidates()) {
                break;
            }
            if (!RASTER.contains(file.mime())
                    || file.width() < MIN_WIDTH
                    || file.imageUrl().isBlank()
                    || file.copyrighted()) {
                continue;
            }
            CoverLicence.basis(file.hiddenCategories(), properties.getMinYearsSinceDeath())
                    .ifPresent(basis -> candidates.add(new CoverCandidate(
                            file.title(),
                            file.pageUrl(),
                            file.imageUrl(),
                            file.mime(),
                            file.artist(),
                            file.licence(),
                            basis)));
        }
        return new CoverCandidates(
                workSlug,
                book.getTitle(),
                book.getAuthor(),
                candidates,
                candidates.isEmpty() ? "No file on Commons passed the licence rule." : "");
    }

    /**
     * Records what a judge decided, replacing whatever was decided before. The chosen file is fetched from Commons
     * here, so what is served is the file the record names and never bytes a caller sent.
     */
    public CoverStatus decide(CoverDecision decision) {
        WorkCover cover = coverRepository.findById(decision.workSlug()).orElseGet(WorkCover::new);
        cover.setWorkSlug(decision.workSlug());
        cover.setCheckedAt(Instant.now(clock));
        clear(cover);
        cover.setReason(decision.reason());
        cover.setModel(decision.model());
        CoverCandidate chosen = decision.chosen();
        if (chosen == null) {
            cover.setStatus(CoverStatus.NONE);
            coverRepository.save(cover);
            return CoverStatus.NONE;
        }
        if (!RASTER.contains(chosen.mime()) || !servedByCommons(chosen.imageUrl())) {
            throw new IllegalArgumentException("A cover is a JPEG or PNG served by Wikimedia Commons");
        }
        byte[] image = commons.download(chosen.imageUrl());
        if (image == null || image.length == 0 || image.length > MAX_BYTES) {
            throw new IllegalArgumentException("The chosen file could not be fetched from Commons");
        }
        cover.setStatus(CoverStatus.APPROVED);
        cover.setImage(image);
        cover.setContentType(chosen.mime());
        cover.setFocusX(decision.focusX());
        cover.setFocusY(decision.focusY());
        cover.setSourcePageUrl(chosen.pageUrl());
        cover.setArtist(chosen.artist());
        cover.setLicence(chosen.licence());
        cover.setLicenceBasis(chosen.licenceBasis());
        coverRepository.save(cover);
        log.info("Cover for {} taken from {}", decision.workSlug(), chosen.pageUrl());
        return CoverStatus.APPROVED;
    }

    /** Looks for one work's cover from here, with the API's vision model, replacing whatever was decided before. */
    public CoverStatus prepare(String workSlug) {
        if (!properties.judgesHere()) {
            throw new IllegalStateException("Covers are chosen on the plan, not from here");
        }
        CoverCandidates offered = candidates(workSlug);
        if (offered.candidates().isEmpty()) {
            return decide(new CoverDecision(workSlug, null, CENTRE, CENTRE, offered.note(), null));
        }
        List<CoverJudge.Shown> shown = new ArrayList<>();
        List<CoverCandidate> shownCandidates = new ArrayList<>();
        for (CoverCandidate candidate : offered.candidates()) {
            byte[] bytes = commons.download(candidate.imageUrl());
            if (bytes != null && bytes.length > 0 && bytes.length <= MAX_BYTES) {
                shown.add(new CoverJudge.Shown(candidate.title(), candidate.mime(), bytes));
                shownCandidates.add(candidate);
            }
        }
        if (shown.isEmpty()) {
            return decide(new CoverDecision(workSlug, null, CENTRE, CENTRE, "No candidate could be downloaded.", null));
        }
        CoverJudge.Verdict verdict = judge.choose(offered.title(), offered.author(), shown);
        return decide(new CoverDecision(
                workSlug,
                verdict.pick() < 0 ? null : shownCandidates.get(verdict.pick()),
                verdict.focusX(),
                verdict.focusY(),
                verdict.reason(),
                verdict.model()));
    }

    /** Puts a work back among those never looked at, so the next run of the judge takes it again. */
    public void forget(String workSlug) {
        coverRepository.deleteById(workSlug);
    }

    /** An operator takes a cover down. The record of what it was stays; the image goes. */
    public void reject(String workSlug) {
        WorkCover cover =
                coverRepository.findById(workSlug).orElseThrow(() -> new EntityNotFoundException("Cover not found"));
        cover.setStatus(CoverStatus.REJECTED);
        cover.setImage(null);
        coverRepository.save(cover);
    }

    private static boolean servedByCommons(String imageUrl) {
        try {
            URI uri = URI.create(imageUrl);
            return "https".equals(uri.getScheme()) && uri.getHost() != null && COMMONS_HOSTS.contains(uri.getHost());
        } catch (IllegalArgumentException malformed) {
            return false;
        }
    }

    private static void clear(WorkCover cover) {
        cover.setImage(null);
        cover.setContentType(null);
        cover.setFocusX(CENTRE);
        cover.setFocusY(CENTRE);
        cover.setSourcePageUrl(null);
        cover.setArtist(null);
        cover.setLicence(null);
        cover.setLicenceBasis(null);
        cover.setReason(null);
        cover.setModel(null);
    }
}
