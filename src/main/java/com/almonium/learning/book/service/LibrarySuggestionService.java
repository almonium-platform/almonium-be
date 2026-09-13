package com.almonium.learning.book.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.response.IngestJobDto;
import com.almonium.learning.book.dto.response.LibraryMatch;
import com.almonium.learning.book.dto.response.LibrarySuggestionDto;
import com.almonium.learning.book.dto.response.LibrarySuggestionQueueDto;
import com.almonium.learning.book.dto.response.LibrarySuggestionRow;
import com.almonium.learning.book.dto.response.ProcessorIngestStatus;
import com.almonium.learning.book.dto.response.StoredFile;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.BookMiniProjection;
import com.almonium.learning.book.model.entity.LibrarySuggestion;
import com.almonium.learning.book.model.entity.UserBookImport;
import com.almonium.learning.book.model.enums.BookImportMetadataStatus;
import com.almonium.learning.book.model.enums.BookImportStatus;
import com.almonium.learning.book.model.enums.LibrarySuggestionStatus;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.book.repository.LibrarySuggestionRepository;
import com.almonium.learning.book.repository.UserBookImportRepository;
import com.almonium.subscription.model.entity.enums.Entitlement;
import com.almonium.subscription.service.EffectiveAccessService;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A private import offered for the public library. The owner suggests and may withdraw; an operator accepts,
 * declines, or points at the copy the library already has. Suggestions of the same work are one row in /ops and are
 * decided together. Nothing here publishes: acceptance starts a processor ingest, and publication comes back through
 * the same webhook every library book uses.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional
public class LibrarySuggestionService {
    /** Published before this many years ago is almost certainly public domain; a hint for the reviewer, never a verdict. */
    static final int PUBLIC_DOMAIN_YEARS = 95;

    LibrarySuggestionRepository repository;
    UserBookImportRepository importRepository;
    BookRepository bookRepository;
    BookProcessorClient processorClient;
    EffectiveAccessService effectiveAccessService;
    BookEmailService bookEmailService;

    // --- the owner ---

    public LibrarySuggestionDto suggest(User user, UUID importId) {
        UserBookImport bookImport =
                importRepository.findByIdAndUserId(importId, user.getId()).orElseThrow(EntityNotFoundException::new);
        if (bookImport.getStatus() != BookImportStatus.READY) {
            throw new BadUserRequestActionException("The book has to finish importing first");
        }
        if (bookImport.getMetadataStatus() != BookImportMetadataStatus.CONFIRMED
                || isBlank(bookImport.getTitle())
                || isBlank(bookImport.getAuthor())
                || bookImport.getLanguage() == null) {
            throw new BadUserRequestActionException("Confirm the title, author and language before suggesting it");
        }
        if (repository.existsByBookImportIdAndStatusIn(bookImport.getId(), LibrarySuggestionStatus.OPEN)) {
            throw new ResourceConflictException("This book has already been suggested");
        }

        LibrarySuggestion suggestion = new LibrarySuggestion();
        suggestion.setBookImport(bookImport);
        suggestion.setUser(user);
        suggestion.setTitle(bookImport.getTitle().trim());
        suggestion.setAuthor(bookImport.getAuthor().trim());
        suggestion.setLanguage(bookImport.getLanguage());
        suggestion.setPublicationYear(bookImport.getPublicationYear());
        suggestion.setDescription(bookImport.getDescription());
        suggestion.setError("");
        suggestion.setStatus(LibrarySuggestionStatus.SUGGESTED);

        // The library may already carry the work; then there is nothing to review and nothing to ingest.
        libraryCopy(suggestion.getTitle(), suggestion.getAuthor()).ifPresent(book -> {
            suggestion.setStatus(LibrarySuggestionStatus.PUBLISHED);
            suggestion.setLibraryBook(book);
            suggestion.setPublishedAt(Instant.now());
        });
        return toDto(repository.save(suggestion));
    }

    public void withdraw(User user, UUID importId) {
        UserBookImport bookImport =
                importRepository.findByIdAndUserId(importId, user.getId()).orElseThrow(EntityNotFoundException::new);
        LibrarySuggestion suggestion = repository
                .findFirstByBookImportIdAndStatusInOrderByCreatedAtDesc(
                        bookImport.getId(), LibrarySuggestionStatus.OPEN)
                .orElseThrow(() -> new EntityNotFoundException("No open suggestion for this book"));
        if (suggestion.getStatus() != LibrarySuggestionStatus.SUGGESTED) {
            throw new ResourceConflictException("This suggestion is already being handled");
        }
        repository.delete(suggestion);
    }

    /** The owner's view, folded into the import; null when the import holds no open suggestion. */
    @Transactional(readOnly = true)
    public LibrarySuggestionDto forImport(UUID importId) {
        return repository
                .findFirstByBookImportIdAndStatusInOrderByCreatedAtDesc(importId, LibrarySuggestionStatus.OPEN)
                .map(this::toDto)
                .orElse(null);
    }

    // --- the operator ---

    public LibrarySuggestionQueueDto queue() {
        List<String> warnings = new ArrayList<>();
        List<LibrarySuggestion> active = repository.findByStatusIn(LibrarySuggestionStatus.OPEN);
        active.stream()
                .filter(suggestion -> suggestion.getStatus() == LibrarySuggestionStatus.INGESTING)
                .forEach(suggestion -> sync(suggestion, warnings));

        Map<UUID, Entitlement> entitlements = effectiveAccessService.entitlementsFor(
                active.stream().map(s -> s.getUser().getId()).collect(Collectors.toSet()));
        Map<String, List<LibrarySuggestion>> groups = active.stream()
                .sorted(Comparator.comparing(LibrarySuggestion::getCreatedAt))
                .collect(Collectors.groupingBy(
                        LibrarySuggestionService::groupKey, LinkedHashMap::new, Collectors.toList()));

        List<LibrarySuggestionRow> rows = new ArrayList<>();
        for (List<LibrarySuggestion> group : groups.values()) {
            LibrarySuggestion representative = group.getFirst();
            long premium = group.stream()
                    .filter(s -> entitlements.getOrDefault(s.getUser().getId(), Entitlement.FREE) != Entitlement.FREE)
                    .count();
            LibraryMatch match = representative.getStatus() == LibrarySuggestionStatus.SUGGESTED
                    ? libraryCopy(representative.getTitle(), representative.getAuthor())
                            .map(book -> new LibraryMatch(book.getId(), book.getEditionSlug(), book.getTitle()))
                            .orElse(null)
                    : null;
            rows.add(new LibrarySuggestionRow(
                    representative.getId(),
                    representative.getTitle(),
                    representative.getAuthor(),
                    representative.getLanguage(),
                    representative.getPublicationYear(),
                    publicDomainHint(representative.getPublicationYear()),
                    group.size(),
                    Math.toIntExact(premium),
                    group.size() - Math.toIntExact(premium),
                    representative.getStatus(),
                    match,
                    ingestJob(representative),
                    representative.getCreatedAt()));
        }
        rows.sort(Comparator.comparingInt((LibrarySuggestionRow row) -> switch (row.status()) {
                    case INGESTING -> 0;
                    case SUGGESTED -> 1;
                    default -> 2;
                })
                .thenComparing(
                        Comparator.comparingInt(LibrarySuggestionRow::imports).reversed()));

        return new LibrarySuggestionQueueDto(
                (int) rows.stream()
                        .filter(r -> r.status() == LibrarySuggestionStatus.SUGGESTED)
                        .count(),
                (int) rows.stream()
                        .filter(r -> r.status() == LibrarySuggestionStatus.INGESTING)
                        .count(),
                Math.toIntExact(repository.countByStatus(LibrarySuggestionStatus.PUBLISHED)),
                Math.toIntExact(repository.countByStatus(LibrarySuggestionStatus.DECLINED)),
                rows,
                warnings);
    }

    /** Posts one ingest job seeded with what the owner vouched for; the whole group waits on it. */
    public LibrarySuggestionRowUpdate accept(User operator, UUID id) {
        LibrarySuggestion representative = find(id);
        List<LibrarySuggestion> group = group(representative, LibrarySuggestionStatus.SUGGESTED);
        ProcessorIngestStatus status = processorClient.startLibraryIngest(
                representative.getId(),
                representative.getBookImport().getId(),
                representative.getUser().getId(),
                representative.getTitle(),
                representative.getAuthor(),
                representative.getDescription(),
                representative.getLanguage(),
                representative.getPublicationYear());
        Instant now = Instant.now();
        for (LibrarySuggestion suggestion : group) {
            suggestion.setStatus(LibrarySuggestionStatus.INGESTING);
            suggestion.setDecidedBy(operator);
            suggestion.setDecidedAt(now);
            applyIngest(suggestion, status);
        }
        repository.saveAll(group);
        return new LibrarySuggestionRowUpdate(representative.getId(), group.size());
    }

    public LibrarySuggestionRowUpdate decline(User operator, UUID id) {
        LibrarySuggestion representative = find(id);
        List<LibrarySuggestion> group =
                group(representative, LibrarySuggestionStatus.SUGGESTED, LibrarySuggestionStatus.INGESTING);
        Instant now = Instant.now();
        group.forEach(suggestion -> {
            suggestion.setStatus(LibrarySuggestionStatus.DECLINED);
            suggestion.setDecidedBy(operator);
            suggestion.setDecidedAt(now);
        });
        repository.saveAll(group);
        bookEmailService.suggestionDeclined(
                group.stream().map(LibrarySuggestion::getUser).toList(), representative.getTitle());
        return new LibrarySuggestionRowUpdate(representative.getId(), group.size());
    }

    /** The duplicate path: no job, the owners are pointed at the copy the library already has. */
    public LibrarySuggestionRowUpdate pointToLibrary(User operator, UUID id, UUID bookId) {
        LibrarySuggestion representative = find(id);
        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Library book not found"));
        List<LibrarySuggestion> group =
                group(representative, LibrarySuggestionStatus.SUGGESTED, LibrarySuggestionStatus.INGESTING);
        settle(group, book, operator);
        return new LibrarySuggestionRowUpdate(representative.getId(), group.size());
    }

    /** Back to the queue; the processor edition is left for staff to purge. */
    public LibrarySuggestionRowUpdate cancel(UUID id) {
        LibrarySuggestion representative = find(id);
        List<LibrarySuggestion> group = group(representative, LibrarySuggestionStatus.INGESTING);
        group.forEach(suggestion -> {
            suggestion.setStatus(LibrarySuggestionStatus.SUGGESTED);
            suggestion.setProcessorEditionId(null);
            suggestion.setProcessorEditionSlug(null);
            suggestion.setPhase(null);
            suggestion.setProgress(0);
            suggestion.setError("");
            suggestion.setDecidedBy(null);
            suggestion.setDecidedAt(null);
        });
        repository.saveAll(group);
        return new LibrarySuggestionRowUpdate(representative.getId(), group.size());
    }

    @Transactional(readOnly = true)
    public StoredFile file(UUID id) {
        LibrarySuggestion suggestion = find(id);
        return processorClient.downloadPrivateImportSource(
                suggestion.getBookImport().getId(), suggestion.getUser().getId());
    }

    /** The publication webhook named our suggestion id: the group is in the library now. */
    public void settlePublished(UUID externalJobId, Book book) {
        Optional<LibrarySuggestion> representative = repository.findById(externalJobId);
        if (representative.isEmpty() || representative.get().getStatus() != LibrarySuggestionStatus.INGESTING) {
            return;
        }
        settle(group(representative.get(), LibrarySuggestionStatus.INGESTING), book, null);
    }

    private void settle(List<LibrarySuggestion> group, Book book, User operator) {
        Instant now = Instant.now();
        for (LibrarySuggestion suggestion : group) {
            suggestion.setStatus(LibrarySuggestionStatus.PUBLISHED);
            suggestion.setLibraryBook(book);
            suggestion.setPublishedAt(now);
            if (operator != null) {
                suggestion.setDecidedBy(operator);
                suggestion.setDecidedAt(now);
            }
        }
        repository.saveAll(group);
        bookEmailService.suggestionPublished(
                group.stream().map(LibrarySuggestion::getUser).toList(), book.getTitle(), book.getEditionSlug());
    }

    private LibrarySuggestion find(UUID id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Suggestion not found"));
    }

    /** Every suggestion of the same work in one of the given states, the representative included. */
    private List<LibrarySuggestion> group(LibrarySuggestion representative, LibrarySuggestionStatus... statuses) {
        List<LibrarySuggestionStatus> allowed = List.of(statuses);
        if (!allowed.contains(representative.getStatus())) {
            throw new ResourceConflictException("This suggestion is %s"
                    .formatted(representative.getStatus().name().toLowerCase(Locale.ROOT)));
        }
        String key = groupKey(representative);
        return repository.findByStatusIn(allowed).stream()
                .filter(suggestion -> groupKey(suggestion).equals(key))
                .toList();
    }

    private void sync(LibrarySuggestion suggestion, List<String> warnings) {
        if (suggestion.getProcessorEditionId() == null) {
            return;
        }
        try {
            applyIngest(suggestion, processorClient.libraryIngestStatus(suggestion.getProcessorEditionId()));
            repository.save(suggestion);
        } catch (ApiIntegrationException exception) {
            warnings.add("The book processor could not be reached for %s; showing what it last said"
                    .formatted(suggestion.getTitle()));
        }
    }

    private static void applyIngest(LibrarySuggestion suggestion, ProcessorIngestStatus status) {
        if (status.editionId() != null) suggestion.setProcessorEditionId(status.editionId());
        if (!status.slug().isBlank()) suggestion.setProcessorEditionSlug(status.slug());
        suggestion.setPhase(status.phase());
        suggestion.setProgress(status.progress());
        suggestion.setError(status.error() == null ? "" : status.error());
    }

    /** Same work in the catalogue: the title slugs to its work slug and the author matches. */
    private Optional<Book> libraryCopy(String title, String author) {
        String slug = Slugs.slugify(title);
        if (slug.isBlank()) {
            return Optional.empty();
        }
        List<Book> candidates = bookRepository.findByWorkSlug(slug).stream()
                .filter(book ->
                        book.getAuthor() != null && book.getAuthor().trim().equalsIgnoreCase(author.trim()))
                .toList();
        return candidates.stream()
                .filter(book -> book.getOriginalBook() == null)
                .findFirst()
                .or(() -> candidates.stream().findFirst());
    }

    static String publicDomainHint(Integer publicationYear) {
        int currentYear = LocalDate.now(ZoneOffset.UTC).getYear();
        return publicationYear != null && publicationYear <= currentYear - PUBLIC_DOMAIN_YEARS ? "pd" : "check";
    }

    static String groupKey(LibrarySuggestion suggestion) {
        return suggestion.getTitle().trim().toLowerCase(Locale.ROOT) + "|"
                + suggestion.getAuthor().trim().toLowerCase(Locale.ROOT);
    }

    private static IngestJobDto ingestJob(LibrarySuggestion suggestion) {
        if (suggestion.getStatus() == LibrarySuggestionStatus.SUGGESTED) {
            return null;
        }
        return new IngestJobDto(
                suggestion.getProcessorEditionId(),
                suggestion.getProcessorEditionSlug(),
                suggestion.getPhase(),
                suggestion.getProgress(),
                suggestion.getError(),
                suggestion.getDecidedAt(),
                suggestion.getLibraryBook() == null
                        ? null
                        : suggestion.getLibraryBook().getId());
    }

    private LibrarySuggestionDto toDto(LibrarySuggestion suggestion) {
        Book book = suggestion.getLibraryBook();
        List<Language> parallel = book == null
                ? List.of()
                : bookRepository.findAvailableLanguagesForBook(book.getId()).stream()
                        .map(BookMiniProjection::getLanguage)
                        .filter(language -> language != book.getLanguage())
                        .distinct()
                        .toList();
        return new LibrarySuggestionDto(
                suggestion.getId(),
                suggestion.getStatus(),
                suggestion.getCreatedAt(),
                book == null ? null : book.getEditionSlug(),
                book == null ? null : book.getTitle(),
                parallel);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /** What a decision touched: the row's id and how many suggestions it settled. */
    public record LibrarySuggestionRowUpdate(UUID id, int suggestions) {}
}
