package com.almonium.learning.book.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.learning.book.dto.request.BookRequestAsk;
import com.almonium.learning.book.dto.response.BookLookupDto;
import com.almonium.learning.book.dto.response.BookRequestDto;
import com.almonium.learning.book.dto.response.BookRequestQueueDto;
import com.almonium.learning.book.dto.response.BookRequestRow;
import com.almonium.learning.book.dto.response.LibraryMatch;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.BookRequest;
import com.almonium.learning.book.model.enums.BookRequestStatus;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.book.repository.BookRequestRepository;
import com.almonium.learning.book.service.GutenbergClient.GutenbergMatch;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
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
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * A reader asking for a book the shelf does not have (G19), and the /ops queue that decides (G20). Asks of the same
 * work and language are one row; an operator takes the row to the editorial catalogue or declines it in one click,
 * and the edition's publication is what settles it and tells every asker. Nothing here is metered: nobody was
 * promised anything.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional
public class BookRequestService {
    /** Published before this many years ago is almost certainly public domain; the same rule the suggestion sheet uses. */
    static final int PUBLIC_DOMAIN_YEARS = LibrarySuggestionService.PUBLIC_DOMAIN_YEARS;

    BookRequestRepository repository;
    BookRepository bookRepository;
    GutenbergClient gutenbergClient;
    BookEmailService bookEmailService;
    NotificationService notificationService;

    /** Where staff add the edition; the row's title, author, language and work are passed along as a prefill. */
    @NonFinal
    @Value("${app.books.editorial-url:${app.books.processor-url}}")
    String editorialUrl;

    // --- the reader ---

    /** What the sheet shows under the title field as the reader types. */
    @Transactional(readOnly = true)
    public BookLookupDto lookup(String query, Language language) {
        TitleAuthor parsed = TitleAuthor.parse(query);
        if (parsed.title().isBlank()) {
            return new BookLookupDto("", "", null, "unknown", 0, null);
        }
        Optional<GutenbergMatch> match = gutenbergClient.find(parsed.title(), parsed.author(), language);
        String title = match.map(GutenbergMatch::title).orElse(parsed.title());
        String author = match.map(GutenbergMatch::author)
                .filter(name -> !name.isBlank())
                .orElse(parsed.author());
        int askers = Math.toIntExact(
                repository.countAskers(normalizedKey(title, author), language, BookRequestStatus.STANDING));
        LibraryMatch onShelf = shelfCopy(title, author, language)
                .map(book -> new LibraryMatch(book.getId(), book.getEditionSlug(), book.getTitle()))
                .orElse(null);
        return new BookLookupDto(
                title,
                author,
                match.map(GutenbergMatch::id).orElse(null),
                match.isPresent() ? "gutenberg" : "unknown",
                askers,
                onShelf);
    }

    public BookRequestDto ask(User user, BookRequestAsk ask) {
        String title = ask.title().trim();
        String author = ask.author() == null ? "" : ask.author().trim();
        String key = normalizedKey(title, author);
        if (repository.existsByUserIdAndNormalizedKeyAndLanguageAndStatusIn(
                user.getId(), key, ask.language(), BookRequestStatus.STANDING)) {
            throw new ResourceConflictException("You’ve asked for this one.");
        }
        BookRequest request = new BookRequest();
        request.setUser(user);
        request.setTitle(title);
        request.setAuthor(author);
        request.setNormalizedKey(key);
        request.setLanguage(ask.language());
        request.setStatus(BookRequestStatus.OPEN);
        request.setWorkSlug(workSlugFor(ask, title, author));
        gutenbergClient.find(title, author, ask.language()).ifPresent(match -> request.setGutenbergId(match.id()));
        return toDto(repository.save(request));
    }

    // --- the operator ---

    @Transactional(readOnly = true)
    public BookRequestQueueDto queue() {
        List<BookRequest> standing = repository.findByStatusIn(BookRequestStatus.STANDING);
        Map<String, List<BookRequest>> groups = standing.stream()
                .sorted(Comparator.comparing(BookRequest::getCreatedAt))
                .collect(Collectors.groupingBy(BookRequestService::groupKey, LinkedHashMap::new, Collectors.toList()));
        List<BookRequestRow> rows = new ArrayList<>();
        for (List<BookRequest> group : groups.values()) {
            rows.add(toRow(group));
        }
        rows.sort(Comparator.comparingInt((BookRequestRow row) -> row.status() == BookRequestStatus.IN_PROGRESS ? 0 : 1)
                .thenComparing(Comparator.comparingInt(BookRequestRow::askers).reversed())
                .thenComparing(BookRequestRow::askedAt));
        return new BookRequestQueueDto(
                (int) rows.stream()
                        .filter(row -> row.status() == BookRequestStatus.OPEN)
                        .count(),
                (int) rows.stream()
                        .filter(row -> row.status() == BookRequestStatus.IN_PROGRESS)
                        .count(),
                Math.toIntExact(repository.countByStatus(BookRequestStatus.PUBLISHED)),
                Math.toIntExact(repository.countByStatus(BookRequestStatus.DECLINED)),
                rows.stream().mapToInt(BookRequestRow::askers).sum(),
                rows);
    }

    /** Staff took the row to the editorial catalogue: it waits there for the edition to publish. */
    public BookRequestRow start(User operator, UUID id) {
        BookRequest representative = find(id);
        List<BookRequest> group = group(representative, BookRequestStatus.OPEN);
        Instant now = Instant.now();
        group.forEach(request -> {
            request.setStatus(BookRequestStatus.IN_PROGRESS);
            request.setDecidedBy(operator);
            request.setDecidedAt(now);
        });
        return toRow(repository.saveAll(group));
    }

    /** One click, no reason, no mail: the row stops counting and the askers get nothing. */
    public BookRequestRow decline(User operator, UUID id) {
        BookRequest representative = find(id);
        List<BookRequest> group = group(representative, BookRequestStatus.OPEN, BookRequestStatus.IN_PROGRESS);
        Instant now = Instant.now();
        group.forEach(request -> {
            request.setStatus(BookRequestStatus.DECLINED);
            request.setDecidedBy(operator);
            request.setDecidedAt(now);
        });
        return toRow(repository.saveAll(group));
    }

    /**
     * A published edition settles every standing ask for its work in its language, whether the ask named the work
     * (a book-page ask) or only its title and author (a sheet ask). Each asker hears once.
     */
    public void settlePublished(Book book) {
        Map<UUID, BookRequest> matched = new LinkedHashMap<>();
        if (book.getWorkSlug() != null) {
            repository
                    .findByWorkSlugAndLanguageAndStatusIn(
                            book.getWorkSlug(), book.getLanguage(), BookRequestStatus.STANDING)
                    .forEach(request -> matched.put(request.getId(), request));
        }
        repository
                .findByNormalizedKeyAndLanguageAndStatusIn(
                        normalizedKey(book.getTitle(), book.getAuthor()),
                        book.getLanguage(),
                        BookRequestStatus.STANDING)
                .forEach(request -> matched.put(request.getId(), request));
        if (matched.isEmpty()) {
            return;
        }
        Instant now = Instant.now();
        for (BookRequest request : matched.values()) {
            request.setStatus(BookRequestStatus.PUBLISHED);
            request.setPublishedBook(book);
            request.setPublishedAt(now);
        }
        repository.saveAll(matched.values());
        String actionPath = "/books/" + book.getEditionSlug();
        Map<UUID, BookRequest> byUser = new LinkedHashMap<>();
        matched.values().forEach(request -> byUser.putIfAbsent(request.getUser().getId(), request));
        for (BookRequest request : byUser.values()) {
            String month = BookEmailService.monthOf(request.getCreatedAt());
            notificationService.notifyOfRequestPublished(
                    request.getUser(), request.getId(), book.getTitle(), month, book.getCoverUrl(), actionPath);
            bookEmailService.requestPublished(request.getUser(), book.getTitle(), month, actionPath);
        }
    }

    // --- helpers ---

    private BookRequest find(UUID id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Request not found"));
    }

    private List<BookRequest> group(BookRequest representative, BookRequestStatus... statuses) {
        List<BookRequestStatus> allowed = List.of(statuses);
        if (!allowed.contains(representative.getStatus())) {
            throw new ResourceConflictException("This request is %s"
                    .formatted(representative
                            .getStatus()
                            .name()
                            .toLowerCase(Locale.ROOT)
                            .replace('_', ' ')));
        }
        return repository.findByNormalizedKeyAndLanguageAndStatusIn(
                representative.getNormalizedKey(), representative.getLanguage(), allowed);
    }

    private BookRequestRow toRow(List<BookRequest> group) {
        List<BookRequest> ordered = group.stream()
                .sorted(Comparator.comparing(BookRequest::getCreatedAt))
                .toList();
        BookRequest oldest = ordered.getFirst();
        String workSlug = ordered.stream()
                .map(BookRequest::getWorkSlug)
                .filter(slug -> slug != null)
                .findFirst()
                .orElse(null);
        Integer gutenbergId = ordered.stream()
                .map(BookRequest::getGutenbergId)
                .filter(id -> id != null)
                .findFirst()
                .orElse(null);
        Integer year = ordered.stream()
                .map(BookRequest::getPublicationYear)
                .filter(value -> value != null)
                .findFirst()
                .orElse(null);
        List<Language> have = workSlug == null
                ? List.of()
                : bookRepository.findByWorkSlug(workSlug).stream()
                        .map(Book::getLanguage)
                        .distinct()
                        .sorted()
                        .toList();
        if (year == null && workSlug != null) {
            year = bookRepository.findByWorkSlug(workSlug).stream()
                    .map(Book::getPublicationYear)
                    .filter(value -> value != null)
                    .findFirst()
                    .orElse(null);
        }
        int askers = (int) ordered.stream()
                .map(request -> request.getUser().getId())
                .distinct()
                .count();
        return new BookRequestRow(
                oldest.getId(),
                oldest.getTitle(),
                oldest.getAuthor(),
                oldest.getLanguage(),
                workSlug,
                have,
                year,
                gutenbergId,
                publicDomain(gutenbergId, year),
                askers,
                oldest.getStatus(),
                editorialUrl(oldest, workSlug),
                ordered.getLast().getCreatedAt(),
                oldest.getDecidedAt());
    }

    /** Gutenberg when the index matched, yes when the year confirms it, unlikely otherwise. */
    static String publicDomain(Integer gutenbergId, Integer publicationYear) {
        if (gutenbergId != null) {
            return "gutenberg";
        }
        int currentYear = LocalDate.now(ZoneOffset.UTC).getYear();
        return publicationYear != null && publicationYear <= currentYear - PUBLIC_DOMAIN_YEARS ? "yes" : "unlikely";
    }

    private String editorialUrl(BookRequest request, String workSlug) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(editorialUrl)
                .queryParam("title", request.getTitle())
                .queryParam("author", request.getAuthor())
                .queryParam("language", request.getLanguage().name().toLowerCase(Locale.ROOT));
        if (workSlug != null) {
            builder.queryParam("work", workSlug);
        }
        if (request.getGutenbergId() != null) {
            builder.queryParam("gutenberg", request.getGutenbergId());
        }
        return builder.encode(StandardCharsets.UTF_8).build().toUriString();
    }

    /** The work slug the ask named, or the one the library already has for this title and author, if any. */
    private String workSlugFor(BookRequestAsk ask, String title, String author) {
        if (ask.workSlug() != null && !ask.workSlug().isBlank()) {
            return ask.workSlug().trim();
        }
        String slug = Slugs.slugify(title);
        if (slug.isBlank()) {
            return null;
        }
        boolean known = bookRepository.findByWorkSlug(slug).stream()
                .anyMatch(book -> author.isBlank() || sameAuthor(book, author));
        return known ? slug : null;
    }

    /** The library copy in the reading language, when the shelf already has this work there. */
    private Optional<Book> shelfCopy(String title, String author, Language language) {
        String slug = Slugs.slugify(title);
        if (slug.isBlank()) {
            return Optional.empty();
        }
        List<Book> candidates = bookRepository.findByWorkSlug(slug).stream()
                .filter(book -> book.getLanguage() == language)
                .filter(book -> author.isBlank() || sameAuthor(book, author))
                .toList();
        return candidates.stream()
                .filter(book -> book.getOriginalBook() == null)
                .findFirst()
                .or(() -> candidates.stream().findFirst());
    }

    private static boolean sameAuthor(Book book, String author) {
        return book.getAuthor() != null && fold(book.getAuthor()).equals(fold(author));
    }

    static String normalizedKey(String title, String author) {
        return fold(title) + "|" + fold(author == null ? "" : author);
    }

    private static String fold(String value) {
        return value.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    private static String groupKey(BookRequest request) {
        return request.getNormalizedKey() + "|" + request.getLanguage();
    }

    private static BookRequestDto toDto(BookRequest request) {
        return new BookRequestDto(
                request.getId(),
                request.getTitle(),
                request.getAuthor(),
                request.getLanguage(),
                request.getStatus(),
                request.getCreatedAt());
    }

    /** "Dracula, Bram Stoker": the title before the first comma, the author after it. */
    record TitleAuthor(String title, String author) {
        static TitleAuthor parse(String query) {
            String trimmed = query == null ? "" : query.trim();
            int comma = trimmed.indexOf(',');
            if (comma < 0) {
                return new TitleAuthor(trimmed, "");
            }
            return new TitleAuthor(
                    trimmed.substring(0, comma).trim(),
                    trimmed.substring(comma + 1).trim());
        }
    }
}
