package com.almonium.learning.catalog.service;

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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    /** Every source of a work, the ones that may be prepared first, originals before translations. */
    public List<TextSourceView> forWork(UUID catalogWorkId) {
        CatalogWork work = catalogWorkRepository
                .findById(catalogWorkId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));
        return store.sources(catalogWorkId).stream()
                .map(source -> view(work, source))
                .sorted(Comparator.comparingInt(
                                (TextSourceView view) -> view.status().ordinal())
                        .thenComparing(TextSourceView::translation)
                        .thenComparing(TextSourceView::kind)
                        .thenComparing(view -> view.label() == null ? "" : view.label()))
                .toList();
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
        CountryVerdict verdict = verdict(work, source);
        return new TextSourceView(
                source.id(),
                source.kind(),
                source.language(),
                source.label(),
                source.translation(),
                source.translators(),
                pageUrl(source),
                downloadUrl(source),
                verdict.status(),
                verdict.exactYear());
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

    static String pageUrl(SourceRow source) {
        return CatalogRecord.GUTENBERG.equals(source.kind())
                ? "https://www.gutenberg.org/ebooks/" + source.reference()
                : source.reference();
    }

    /** Gutenberg's EPUB 3 with images; for Wikisource, its export tool's EPUB of the page and its subpages. */
    static String downloadUrl(SourceRow source) {
        if (CatalogRecord.GUTENBERG.equals(source.kind())) {
            return "https://www.gutenberg.org/ebooks/" + source.reference() + ".epub3.images";
        }
        String path = URI.create(source.reference()).getRawPath();
        String page = path.substring(path.lastIndexOf('/') + 1);
        String title = URLDecoder.decode(page, StandardCharsets.UTF_8);
        return WIKISOURCE_EXPORT + "?lang=" + source.language() + "&page="
                + URLEncoder.encode(title, StandardCharsets.UTF_8) + "&format=epub-3";
    }
}
