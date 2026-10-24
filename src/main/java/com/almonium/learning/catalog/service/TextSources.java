package com.almonium.learning.catalog.service;

import com.almonium.learning.catalog.dto.response.SourceOption;
import com.almonium.learning.catalog.dto.response.TextSourceView;
import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogStore;
import com.almonium.learning.catalog.repository.CatalogStore.SourceRow;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.rights.model.CountryVerdict;
import com.almonium.learning.rights.model.RightsFacts;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.model.YearRange;
import com.almonium.learning.rights.service.ProductionRights;
import jakarta.persistence.EntityNotFoundException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Where a catalogue work's text can be fetched, legally and through an API, and whether each text may be prepared:
 * Project Gutenberg's EPUBs by book id, and Wikisource pages through Wikisource's own EPUB export. A source is free
 * only when the work is free where we make books and, for a translation, its translators are too.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TextSources {
    static final String WIKISOURCE_EXPORT = "https://ws-export.wmcloud.org/";

    private final CatalogStore store;
    private final CatalogWorkRepository catalogWorkRepository;
    private final ProductionRights productionRights;
    private final SourceOrder sourceOrder;

    /**
     * Every source of a work, best first: the ones that may be prepared, then files the processor found sound in
     * shape before ones it found suspect, then one edition's text before a page that may only list editions,
     * originals before translations, the libraries in the order the source's language prefers, and a file measured
     * sound before one never measured. The first is the default a reader's want is made from.
     */
    public List<TextSourceView> forWork(UUID catalogWorkId) {
        CatalogWork work = catalogWorkRepository
                .findById(catalogWorkId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));
        List<SourceRow> rows = store.sources(catalogWorkId);
        Map<String, String> shapes =
                store.shapes(rows.stream().map(TextSources::downloadUrl).toList());
        return rows.stream()
                .map(source -> view(work, source, shapes.get(downloadUrl(source))))
                .sorted(Comparator.comparingInt(
                                (TextSourceView view) -> view.status().ordinal())
                        .thenComparing(view -> view.shape() != null && SUSPECT_SHAPES.contains(view.shape()))
                        .thenComparing(view -> !view.edition())
                        .thenComparing(TextSourceView::translation)
                        .thenComparingInt(view -> sourceOrder.rank(view.language(), view.kind()))
                        .thenComparing(view -> !"ok".equals(view.shape()))
                        .thenComparing(view -> view.label() == null ? "" : view.label()))
                .toList();
    }

    static final Set<String> SUSPECT_SHAPES = Set.of("suspect", "unreadable");

    /**
     * The sources a reader may choose from, best first, with the first that may be prepared marked as the default
     * (W1). Protected texts are listed so the reader sees why a library's edition is not offered.
     */
    public List<SourceOption> options(UUID catalogWorkId) {
        List<TextSourceView> ranked = forWork(catalogWorkId);
        TextSourceView preferred = ranked.stream()
                .filter(view -> view.status() == RightsStatus.FREE)
                .findFirst()
                .orElse(null);
        return ranked.stream()
                .map(view -> new SourceOption(
                        view.kind(),
                        view.language(),
                        view.label(),
                        view.translation(),
                        view.pageUrl(),
                        view.downloadUrl(),
                        view.status(),
                        view.shape(),
                        view == preferred))
                .toList();
    }

    /** A work's source by the address the processor fetches it from, the key a want names its choice by. */
    public Optional<TextSourceView> byDownloadUrl(UUID catalogWorkId, String downloadUrl) {
        return forWork(catalogWorkId).stream()
                .filter(view -> view.downloadUrl().equals(downloadUrl))
                .findFirst();
    }

    /** Keep the processor's verdict on the shape of the file at {@code downloadUrl}; a later one replaces it. */
    @Transactional
    public void recordShape(String downloadUrl, String verdict, Instant checkedAt) {
        store.recordShape(downloadUrl, verdict, checkedAt);
    }

    public Optional<TextSourceView> find(UUID sourceId) {
        return store.source(sourceId)
                .flatMap(source ->
                        catalogWorkRepository.findById(source.workId()).map(work -> view(work, source)));
    }

    /** Whether a source is one of this work's: a source id from another work cannot start this one. */
    public boolean belongsTo(TextSourceView source, UUID catalogWorkId) {
        return store.sources(catalogWorkId).stream().anyMatch(row -> row.id().equals(source.id()));
    }

    TextSourceView view(CatalogWork work, SourceRow source) {
        return view(work, source, null);
    }

    TextSourceView view(CatalogWork work, SourceRow source, String shape) {
        CountryVerdict verdict = verdict(work, source);
        return new TextSourceView(
                source.id(),
                source.kind(),
                source.language(),
                source.label(),
                source.translation(),
                source.translators(),
                source.edition(),
                pageUrl(source),
                downloadUrl(source),
                verdict.status(),
                verdict.exactYear(),
                shape);
    }

    /** The work's verdict, then the translators', the stricter winning; a translation by nobody known is unknown. */
    CountryVerdict verdict(CatalogWork work, SourceRow source) {
        CountryVerdict original = productionRights.verdict(work.rightsFacts());
        if (!source.translation() || original.status() == RightsStatus.PROTECTED) {
            return original;
        }
        if (source.translators().isEmpty()) {
            return new CountryVerdict(original.country(), RightsStatus.UNKNOWN, original.freeFrom());
        }
        CountryVerdict translation = productionRights.verdict(new RightsFacts(
                null, source.translators().stream().map(CatalogAuthor::facts).toList()));
        RightsStatus status;
        if (translation.status() == RightsStatus.PROTECTED) {
            status = RightsStatus.PROTECTED;
        } else if (translation.status() == RightsStatus.UNKNOWN || original.status() == RightsStatus.UNKNOWN) {
            status = RightsStatus.UNKNOWN;
        } else {
            status = RightsStatus.FREE;
        }
        return new CountryVerdict(original.country(), status, later(original.freeFrom(), translation.freeFrom()));
    }

    /** The text is free when both the original and the translation are: the later of the two. */
    static YearRange later(YearRange a, YearRange b) {
        Integer earliest = a.earliest() == null
                ? b.earliest()
                : b.earliest() == null ? a.earliest() : Math.max(a.earliest(), b.earliest());
        Integer latest = a.latest() == null || b.latest() == null ? null : Math.max(a.latest(), b.latest());
        return new YearRange(earliest, latest);
    }

    /**
     * The human page of a source. Gutenberg and Wolne Lektury are referenced by id and slug, Standard Ebooks by its
     * path, ELTeC by the raw TEI file's address, BEQ and ELG by the EPUB's address; Wikisource by the page's address.
     */
    static String pageUrl(SourceRow source) {
        return switch (source.kind()) {
            case CatalogRecord.GUTENBERG -> "https://www.gutenberg.org/ebooks/" + source.reference();
            case CatalogRecord.STANDARD_EBOOKS -> "https://standardebooks.org" + source.reference();
            case CatalogRecord.WOLNE_LEKTURY -> "https://wolnelektury.pl/katalog/lektura/" + source.reference() + "/";
            default -> source.reference();
        };
    }

    /**
     * The file the book processor fetches: Gutenberg's EPUB 3 with images (the processor falls back to the plain
     * build), Standard Ebooks' EPUB with the parameter without which the site answers a page, Wolne Lektury's EPUB
     * from its media path, ELTeC's TEI, BEQ's and ELG's EPUB as referenced, and for Wikisource its export tool's
     * EPUB of the page and its subpages.
     */
    static String downloadUrl(SourceRow source) {
        switch (source.kind()) {
            case CatalogRecord.GUTENBERG -> {
                return "https://www.gutenberg.org/ebooks/" + source.reference() + ".epub3.images";
            }
            case CatalogRecord.STANDARD_EBOOKS -> {
                String[] parts = source.reference().split("/");
                String file = parts[parts.length - 2] + "_" + parts[parts.length - 1] + ".epub";
                return "https://standardebooks.org" + source.reference() + "/downloads/" + file + "?source=download";
            }
            case CatalogRecord.WOLNE_LEKTURY -> {
                return "https://wolnelektury.pl/media/book/epub/" + source.reference() + ".epub";
            }
            case CatalogRecord.ELTEC, CatalogRecord.BEQ, CatalogRecord.ELG -> {
                return source.reference();
            }
            default -> {}
        }
        String path = URI.create(source.reference()).getRawPath();
        String page = path.substring(path.lastIndexOf('/') + 1);
        String title = URLDecoder.decode(page, StandardCharsets.UTF_8);
        return WIKISOURCE_EXPORT + "?lang=" + source.language() + "&page="
                + URLEncoder.encode(title, StandardCharsets.UTF_8) + "&format=epub-3";
    }
}
