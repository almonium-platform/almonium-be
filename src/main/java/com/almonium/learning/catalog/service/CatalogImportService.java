package com.almonium.learning.catalog.service;

import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.catalog.repository.CatalogStore;
import com.almonium.learning.catalog.repository.CatalogStore.SourceRow;
import com.almonium.learning.catalog.repository.CatalogStore.TitleRow;
import com.almonium.learning.catalog.repository.CatalogStore.WorkRow;
import com.almonium.learning.catalog.service.GutenbergCatalogSource.Text;
import com.almonium.learning.catalog.service.WikidataCatalogSource.Label;
import com.almonium.learning.catalog.service.WikidataCatalogSource.Person;
import com.almonium.learning.catalog.service.WikidataCatalogSource.Snapshot;
import com.almonium.learning.catalog.service.WikidataCatalogSource.WikisourcePage;
import com.almonium.learning.catalog.service.WikidataCatalogSource.Work;
import com.almonium.learning.rights.model.CountryVerdict;
import com.almonium.learning.rights.model.RightsFacts;
import com.almonium.learning.rights.service.ProductionRights;
import com.github.f4b6a3.uuid.UuidCreator;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Builds the catalogue from Wikidata and Project Gutenberg and writes it in one transaction, so search never sees a
 * half-written catalogue. Wikidata gives the notable works and their authors' lives; Gutenberg adds a text id where a
 * work has one, fills death years Wikidata lacks, and contributes the texts no notable work matches. Ids survive a
 * re-import; shelf links ({@code work_slug}) are never overwritten, only filled.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogImportService {
    private final WikidataCatalogSource wikidata;
    private final GutenbergCatalogSource gutenberg;
    private final List<LibraryCatalogSource> libraries;
    private final CatalogStore store;
    private final BookRepository bookRepository;
    private final CatalogMatcher catalogMatcher;
    private final ProductionRights productionRights;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationEventPublisher events;
    private final Clock clock;

    private final AtomicReference<ImportRun> lastRun = new AtomicReference<>(ImportRun.never());

    /** How the latest import went, for /ops. */
    public record ImportRun(
            String state, Instant startedAt, Instant finishedAt, int works, int titles, int linked, String error) {
        static ImportRun never() {
            return new ImportRun("NEVER", null, null, 0, 0, 0, null);
        }
    }

    public ImportRun lastRun() {
        return lastRun.get();
    }

    /** Starts an import on its own thread unless one is running; returns at once. */
    public ImportRun startImport() {
        ImportRun current = lastRun.get();
        if ("RUNNING".equals(current.state())) {
            return current;
        }
        ImportRun running = new ImportRun("RUNNING", Instant.now(clock), null, 0, 0, 0, null);
        if (!lastRun.compareAndSet(current, running)) {
            return lastRun.get();
        }
        Thread.ofVirtual().name("catalog-import").start(this::runImport);
        return running;
    }

    void runImport() {
        Instant started = lastRun.get().startedAt() == null
                ? Instant.now(clock)
                : lastRun.get().startedAt();
        try {
            ImportRun done = importNow(started);
            lastRun.set(done);
        } catch (RuntimeException exception) {
            log.error("Catalogue import failed", exception);
            lastRun.set(new ImportRun(
                    "FAILED", started, Instant.now(clock), 0, 0, 0, String.valueOf(exception.getMessage())));
        }
    }

    ImportRun importNow(Instant started) {
        Snapshot snapshot = wikidata.fetch();
        List<Text> texts = gutenberg.fetch();
        Map<String, UUID> wikidataIds = store.idsByWikidata();
        Map<Integer, UUID> gutenbergIds = store.idsByGutenbergOnly();

        List<CatalogRecord> records = fromWikidata(snapshot, wikidataIds);
        records.addAll(mergeGutenberg(records, texts, gutenbergIds));
        for (LibraryCatalogSource library : libraries) {
            try {
                int attached = attachLibraryTexts(records, library.fetch());
                log.info("{}: {} texts attached to works", library.kind(), attached);
            } catch (RuntimeException exception) {
                // a library that is down loses its sources for this import, never the import
                log.warn("{} could not be read: {}", library.kind(), exception.getMessage());
            }
        }

        int year = productionRights.currentYear();
        List<WorkRow> works = new ArrayList<>(records.size());
        List<TitleRow> titles = new ArrayList<>();
        List<SourceRow> sources = new ArrayList<>();
        for (CatalogRecord record : records) {
            CountryVerdict verdict = productionRights.verdict(
                    new RightsFacts(
                            record.publicationYear,
                            record.authors.stream().map(CatalogAuthor::facts).toList()),
                    year);
            works.add(new WorkRow(
                    record.id,
                    record.wikidataId,
                    record.gutenbergId,
                    CatalogRecord.truncate(record.title, 1000),
                    record.originalTitle == null ? null : CatalogRecord.truncate(record.originalTitle, 1000),
                    language(record.originalLanguage),
                    record.publicationYear,
                    record.authorDisplay(),
                    record.authors,
                    record.popularity,
                    verdict.status(),
                    verdict.exactYear(),
                    record.publisher,
                    record.literary));
            titles.addAll(titleRows(record));
            for (CatalogRecord.Source source : record.sources) {
                sources.add(new SourceRow(
                        UuidCreator.getTimeOrderedEpoch(),
                        record.id,
                        source.kind(),
                        language(source.language()),
                        CatalogRecord.truncate(source.reference(), 1000),
                        source.label() == null ? null : CatalogRecord.truncate(source.label(), 1000),
                        source.translation(),
                        source.translators(),
                        source.edition()));
            }
        }
        Instant writeStarted = Instant.now(clock);
        Integer deleted = transactionTemplate.execute(status -> {
            store.clearGutenbergIds();
            store.upsertWorks(works, writeStarted);
            store.replaceTitles(titles);
            store.replaceSources(sources);
            return store.deleteStale(writeStarted);
        });
        int linked = linkShelf();
        events.publishEvent(new CatalogImportedEvent(works.size()));
        log.info(
                "Catalogue import wrote {} works and {} titles, removed {} stale works, linked {} shelf works",
                works.size(),
                titles.size(),
                deleted,
                linked);
        return new ImportRun("DONE", started, Instant.now(clock), works.size(), titles.size(), linked, null);
    }

    /**
     * Recomputes the production status of every work, which changes only as the years turn; writes only the rows
     * whose answer changed. Returns how many changed.
     */
    public int refreshProductionStatus() {
        int year = productionRights.currentYear();
        List<CatalogStore.FactsRow> changed = new ArrayList<>();
        for (CatalogStore.FactsRow row : store.allFacts()) {
            List<CatalogAuthor> authors = store.readAuthors(row.authorsJson());
            CountryVerdict verdict = productionRights.verdict(
                    new RightsFacts(
                            row.publicationYear(),
                            authors.stream().map(CatalogAuthor::facts).toList()),
                    year);
            String status = verdict.status().name();
            Integer freeFrom = verdict.exactYear();
            if (!status.equals(row.status()) || !Objects.equals(freeFrom, row.freeFrom())) {
                changed.add(new CatalogStore.FactsRow(row.id(), row.publicationYear(), null, status, freeFrom));
            }
        }
        transactionTemplate.executeWithoutResult(status -> store.updateProductionStatus(changed));
        return changed.size();
    }

    /** Whether the catalogue has any works at all; an empty one is imported on startup. */
    public boolean isEmpty() {
        return store.idsByWikidata().isEmpty() && store.idsByGutenbergOnly().isEmpty();
    }

    /** Every notable Wikidata work that has a title in some language, with its authors' facts. */
    static List<CatalogRecord> fromWikidata(Snapshot snapshot, Map<String, UUID> existingIds) {
        List<CatalogRecord> records = new ArrayList<>(snapshot.works().size());
        for (Work work : snapshot.works().values()) {
            List<Label> labels = snapshot.labelsByWork().getOrDefault(work.id(), List.of());
            String title = pickTitle(labels, work.languageCode());
            if (title == null) {
                continue;
            }
            CatalogRecord record = new CatalogRecord();
            record.id = existingIds.getOrDefault(work.id(), UuidCreator.getTimeOrderedEpoch());
            record.wikidataId = work.id();
            record.gutenbergId = snapshot.gutenbergByWork().get(work.id());
            record.title = title;
            record.originalLanguage = work.languageCode();
            record.originalTitle = labels.stream()
                    .filter(label -> label.language().equals(work.languageCode()))
                    .map(Label::text)
                    .findFirst()
                    .orElse(null);
            record.publicationYear = work.year();
            record.popularity = work.popularity();
            record.literary = work.literary();
            String publisher = snapshot.publisherByWork().get(work.id());
            record.publisher = publisher == null ? null : CatalogRecord.truncate(publisher, 500);
            for (String authorId : snapshot.authorsByWork().getOrDefault(work.id(), List.of())) {
                Person person = snapshot.people().get(authorId);
                if (person != null) {
                    record.authors.add(new CatalogAuthor(
                            person.name(),
                            person.id(),
                            person.birthYear(),
                            person.deathYear(),
                            person.diedForFrance(),
                            null,
                            person.repressed()));
                }
            }
            Set<String> seen = new HashSet<>();
            for (Label label : labels) {
                if (seen.add(TitleFolding.fold(label.text()))) {
                    record.addTitle(label.text(), label.language());
                }
            }
            for (WikisourcePage page : snapshot.wikisourceByWork().getOrDefault(work.id(), List.of())) {
                List<CatalogAuthor> translators = page.translatorIds().stream()
                        .map(snapshot.people()::get)
                        .filter(Objects::nonNull)
                        .map(person -> new CatalogAuthor(
                                person.name(),
                                person.id(),
                                person.birthYear(),
                                person.deathYear(),
                                person.diedForFrance(),
                                null,
                                person.repressed()))
                        .toList();
                // A page in another language than the work's (or when the work's is not recorded) is a translation.
                boolean translation = !translators.isEmpty()
                        || record.originalLanguage == null
                        || !record.originalLanguage.equals(page.language());
                record.sources.add(new CatalogRecord.Source(
                        CatalogRecord.WIKISOURCE,
                        page.language(),
                        page.url(),
                        wikisourceTitle(page.url()),
                        translation,
                        translators,
                        page.editionId() != null));
            }
            records.add(record);
        }
        // Wikidata sometimes gives two works the same Gutenberg text; the better-known one keeps it.
        Set<Integer> taken = new HashSet<>();
        records.stream()
                .filter(record -> record.gutenbergId != null)
                .sorted(Comparator.comparingInt((CatalogRecord record) -> record.popularity)
                        .reversed())
                .forEach(record -> {
                    if (!taken.add(record.gutenbergId)) {
                        record.gutenbergId = null;
                    }
                });
        return records;
    }

    /** English first, then the multilingual label, then the original language, then any. */
    static String pickTitle(List<Label> labels, String originalLanguage) {
        for (String language : new String[] {"en", "mul", originalLanguage}) {
            for (Label label : labels) {
                if (language != null
                        && language.equals(label.language())
                        && !label.text().isBlank()) {
                    return label.text();
                }
            }
        }
        return labels.stream()
                .map(Label::text)
                .filter(text -> !text.isBlank())
                .findFirst()
                .orElse(null);
    }

    /**
     * Attaches each Gutenberg text to the notable work it is a copy of (by Wikidata's Gutenberg id, else by title and
     * author surname), and returns new records for the texts nothing matched, one per title and first author.
     */
    static List<CatalogRecord> mergeGutenberg(
            List<CatalogRecord> records, List<Text> texts, Map<Integer, UUID> existingIds) {
        Map<Integer, CatalogRecord> byGutenberg = new HashMap<>();
        Map<String, List<CatalogRecord>> byTitle = new HashMap<>();
        for (CatalogRecord record : records) {
            if (record.gutenbergId != null) {
                byGutenberg.putIfAbsent(record.gutenbergId, record);
            }
            for (CatalogRecord.Title title : record.titles) {
                byTitle.computeIfAbsent(
                                TitleFolding.fold(TitleFolding.mainTitle(title.text())), key -> new ArrayList<>())
                        .add(record);
            }
        }
        Set<Integer> claimed = new HashSet<>(byGutenberg.keySet());
        Map<String, CatalogRecord> unmatched = new LinkedHashMap<>();
        for (Text text :
                texts.stream().sorted(Comparator.comparingInt(Text::id)).toList()) {
            String mainTitle = TitleFolding.mainTitle(text.title());
            String foldedTitle = TitleFolding.fold(mainTitle);
            if (foldedTitle.isEmpty()) {
                continue;
            }
            CatalogRecord match = byGutenberg.get(text.id());
            if (match == null) {
                match = byTitle.getOrDefault(foldedTitle, List.of()).stream()
                        .filter(candidate -> sameAuthors(candidate, text))
                        .max(Comparator.comparingInt(candidate -> candidate.popularity))
                        .orElse(null);
            }
            if (match != null) {
                if (match.gutenbergId == null && claimed.add(text.id())) {
                    match.gutenbergId = text.id();
                }
                fillDeathYears(match, text);
                match.sources.add(gutenbergSource(text, match.originalLanguage));
                continue;
            }
            String key = foldedTitle + "|"
                    + (text.authors().isEmpty()
                            ? ""
                            : TitleFolding.fold(text.authors().getFirst().surname()));
            CatalogRecord record = unmatched.get(key);
            if (record == null) {
                record = new CatalogRecord();
                record.id = existingIds.getOrDefault(text.id(), UuidCreator.getTimeOrderedEpoch());
                record.gutenbergId = claimed.add(text.id()) ? text.id() : null;
                record.title = mainTitle;
                record.originalLanguage = text.language().isEmpty() ? null : text.language();
                for (GutenbergCatalogSource.Author author : text.authors()) {
                    record.authors.add(new CatalogAuthor(
                            author.name(), null, author.birthYear(), author.deathYear(), false, null));
                }
                record.addTitle(mainTitle, record.originalLanguage);
                unmatched.put(key, record);
            }
            record.sources.add(gutenbergSource(text, record.originalLanguage));
        }
        return new ArrayList<>(unmatched.values());
    }

    /**
     * Attaches a library's texts to the works they are copies of, by folded title and author surname, as sources of
     * the library's kind. A text that matches no work is left out: a library rarely records the dates the copyright
     * engine needs to make a work of it. A text whose language is not the work's is a translation by translators the
     * library does not name, which the rights engine judges unknown. Returns how many were attached.
     */
    static int attachLibraryTexts(List<CatalogRecord> records, List<LibraryCatalogSource.LibraryText> texts) {
        Map<String, List<CatalogRecord>> byTitle = new HashMap<>();
        for (CatalogRecord record : records) {
            for (CatalogRecord.Title title : record.titles) {
                byTitle.computeIfAbsent(
                                TitleFolding.fold(TitleFolding.mainTitle(title.text())), key -> new ArrayList<>())
                        .add(record);
            }
        }
        int attached = 0;
        for (LibraryCatalogSource.LibraryText text : texts) {
            String folded = TitleFolding.fold(TitleFolding.mainTitle(text.title()));
            if (folded.isEmpty()) {
                continue;
            }
            String surname = surname(text.author());
            CatalogRecord match = byTitle.getOrDefault(folded, List.of()).stream()
                    .filter(candidate -> sameAuthor(candidate, surname))
                    .max(Comparator.comparingInt(candidate -> candidate.popularity))
                    .orElse(null);
            if (match == null) {
                continue;
            }
            boolean translation = match.originalLanguage != null
                    && text.language() != null
                    && !match.originalLanguage.equals(text.language());
            match.sources.add(new CatalogRecord.Source(
                    text.kind(), text.language(), text.reference(), text.title(), translation, List.of(), true));
            attached++;
        }
        return attached;
    }

    /** The last word of a name as a library writes it: "Honoré de Balzac" is matched on "balzac". */
    static String surname(String author) {
        String[] words = TitleFolding.fold(author == null ? "" : author).trim().split(" ");
        return words.length == 0 ? "" : words[words.length - 1];
    }

    static boolean sameAuthor(CatalogRecord record, String surname) {
        if (surname.isBlank()) {
            return record.authors.isEmpty();
        }
        return record.authors.stream()
                .anyMatch(author -> (" " + TitleFolding.fold(author.name()) + " ").contains(" " + surname + " "));
    }

    /**
     * A Gutenberg text as a source: a translation when Gutenberg names translators or when its language is not the
     * work's (a translation whose translators Gutenberg does not name is judged unknown, never free).
     */
    static CatalogRecord.Source gutenbergSource(Text text, String workLanguage) {
        String language = text.language().isEmpty() ? null : text.language();
        boolean translation = !text.translators().isEmpty()
                || (workLanguage != null && language != null && !workLanguage.equals(language));
        List<CatalogAuthor> translators = text.translators().stream()
                .map(author ->
                        new CatalogAuthor(author.name(), null, author.birthYear(), author.deathYear(), false, null))
                .toList();
        return new CatalogRecord.Source(
                CatalogRecord.GUTENBERG,
                language,
                String.valueOf(text.id()),
                TitleFolding.mainTitle(text.title()),
                translation,
                translators,
                true);
    }

    /** "Guerre_et_Paix_(trad._Bienstock)" from a page URL, readable: "Guerre et Paix (trad. Bienstock)". */
    static String wikisourceTitle(String url) {
        String path = URI.create(url).getRawPath();
        String page = path == null ? url : path.substring(path.lastIndexOf('/') + 1);
        return URLDecoder.decode(page, StandardCharsets.UTF_8).replace('_', ' ');
    }

    /** Every Gutenberg author's surname appears among the work's authors; a text with no author matches none. */
    static boolean sameAuthors(CatalogRecord record, Text text) {
        if (text.authors().isEmpty()) {
            return record.authors.isEmpty();
        }
        List<String> names = record.authors.stream()
                .map(author -> " " + TitleFolding.fold(author.name()) + " ")
                .toList();
        return text.authors().stream()
                .map(author -> " " + TitleFolding.fold(author.surname()) + " ")
                .allMatch(surname -> !surname.isBlank() && names.stream().anyMatch(name -> name.contains(surname)));
    }

    /** Wikidata often lacks a death year Gutenberg has; take it when the surname agrees. */
    static void fillDeathYears(CatalogRecord record, Text text) {
        List<CatalogAuthor> filled = new ArrayList<>(record.authors.size());
        for (CatalogAuthor author : record.authors) {
            CatalogAuthor result = author;
            if (author.deathYear() == null) {
                String name = " " + TitleFolding.fold(author.name()) + " ";
                for (GutenbergCatalogSource.Author source : text.authors()) {
                    if (source.deathYear() != null && name.contains(" " + TitleFolding.fold(source.surname()) + " ")) {
                        result = new CatalogAuthor(
                                author.name(),
                                author.wikidataId(),
                                author.birthYear() != null ? author.birthYear() : source.birthYear(),
                                source.deathYear(),
                                author.diedForFrance(),
                                author.rehabilitatedYear(),
                                author.repressed());
                        break;
                    }
                }
            }
            filled.add(result);
        }
        record.authors = filled;
    }

    static List<TitleRow> titleRows(CatalogRecord record) {
        List<TitleRow> rows = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (CatalogRecord.Title title : record.titles) {
            String folded = TitleFolding.fold(title.text());
            if (!folded.isEmpty() && seen.add(CatalogRecord.TITLE + folded)) {
                rows.add(new TitleRow(
                        record.id, title.text(), language(title.language()), CatalogRecord.TITLE, cap(folded)));
            }
        }
        String authors = record.authorDisplay();
        if (authors != null) {
            for (CatalogAuthor author : record.authors) {
                String folded = TitleFolding.fold(author.name());
                if (!folded.isEmpty() && seen.add(CatalogRecord.AUTHOR + folded)) {
                    rows.add(new TitleRow(record.id, author.name(), null, CatalogRecord.AUTHOR, cap(folded)));
                }
            }
            // "dracula stoker": one row with both, so a query naming title and author matches as a whole.
            String full = record.title + " " + authors;
            rows.add(new TitleRow(
                    record.id,
                    CatalogRecord.truncate(full, 1000),
                    null,
                    CatalogRecord.FULL,
                    cap(TitleFolding.fold(full))));
        }
        return rows;
    }

    /** Points every shelf work not yet linked at the catalogue work it is an edition of. Returns how many were linked. */
    int linkShelf() {
        int linked = 0;
        Set<String> done = new HashSet<>();
        for (Book book : bookRepository.findAll()) {
            String slug = book.getWorkSlug();
            if (slug == null || book.getOriginalBook() != null || !done.add(slug) || store.isLinked(slug)) {
                continue;
            }
            if (catalogMatcher
                    .linkOrMatch(slug, book.getTitle(), book.getAuthor())
                    .isPresent()) {
                linked++;
            }
        }
        return linked;
    }

    /** Wikimedia language codes run long ("zh-classical", "be-tarask"); the column keeps sixteen characters. */
    private static String language(String code) {
        return code == null || code.isEmpty() ? null : CatalogRecord.truncate(code, 16);
    }

    private static String cap(String folded) {
        return CatalogRecord.truncate(folded, 1000);
    }
}
