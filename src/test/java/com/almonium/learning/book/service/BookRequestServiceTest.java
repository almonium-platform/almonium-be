package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.learning.book.dto.request.BookRequestAsk;
import com.almonium.learning.book.dto.response.BookLookupDto;
import com.almonium.learning.book.dto.response.BookRequestQueueDto;
import com.almonium.learning.book.dto.response.BookRequestRow;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.BookRequest;
import com.almonium.learning.book.model.enums.BookRequestStatus;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.book.repository.BookRequestRepository;
import com.almonium.learning.book.service.GutenbergClient.GutenbergMatch;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BookRequestServiceTest {
    @Mock
    BookRequestRepository repository;

    @Mock
    BookRepository bookRepository;

    @Mock
    GutenbergClient gutenbergClient;

    @Mock
    BookEmailService bookEmailService;

    @Mock
    NotificationService notificationService;

    @InjectMocks
    BookRequestService service;

    @BeforeEach
    void editorialCatalogue() {
        ReflectionTestUtils.setField(service, "editorialUrl", "https://books.example.test/editions/new");
    }

    @Test
    void theLookupTellsTheReaderWhatTheIndexKnowsAndHowManyAsked() {
        when(gutenbergClient.find("Dracula", "Bram Stoker", Language.EN))
                .thenReturn(Optional.of(new GutenbergMatch(345, "Dracula", "Stoker, Bram", 1912)));
        when(repository.countAskers("dracula|stoker, bram", Language.EN, BookRequestStatus.STANDING))
                .thenReturn(12L);
        when(bookRepository.findByWorkSlug("dracula")).thenReturn(List.of());

        BookLookupDto lookup = service.lookup("Dracula, Bram Stoker", Language.EN);

        assertThat(lookup.gutenbergId()).isEqualTo(345);
        assertThat(lookup.publicDomain()).isEqualTo("gutenberg");
        assertThat(lookup.askers()).isEqualTo(12);
        assertThat(lookup.onShelf()).isNull();
    }

    @Test
    void aWorkAlreadyOnTheShelfInThatLanguageIsPointedAtInsteadOfAskedFor() {
        Book copy = book("frankenstein-en-c1", "frankenstein", "Frankenstein", "Mary Shelley", Language.EN);
        when(gutenbergClient.find(anyString(), anyString(), any())).thenReturn(Optional.empty());
        when(repository.countAskers(anyString(), any(), anyCollection())).thenReturn(0L);
        when(bookRepository.findByWorkSlug("frankenstein")).thenReturn(List.of(copy));

        BookLookupDto lookup = service.lookup("Frankenstein, Mary Shelley", Language.EN);

        assertThat(lookup.publicDomain()).isEqualTo("unknown");
        assertThat(lookup.onShelf()).isNotNull();
        assertThat(lookup.onShelf().editionSlug()).isEqualTo("frankenstein-en-c1");
    }

    @Test
    void askingTwiceForTheSameWorkIsRefusedInTheReadersWords() {
        User user = user();
        when(repository.existsByUserIdAndNormalizedKeyAndLanguageAndStatusIn(
                        user.getId(), "dracula|bram stoker", Language.EN, BookRequestStatus.STANDING))
                .thenReturn(true);

        assertThatThrownBy(() -> service.ask(user, new BookRequestAsk("Dracula", "Bram Stoker", Language.EN, null)))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessage("You’ve asked for this one.");
        verify(repository, never()).save(any());
    }

    @Test
    void anAskRemembersTheIndexMatchAndTheWorkTheLibraryAlreadyHas() {
        User user = user();
        Book polish = book("frankenstein-pl", "frankenstein", "Frankenstein", "Mary Shelley", Language.PL);
        when(repository.existsByUserIdAndNormalizedKeyAndLanguageAndStatusIn(
                        any(), anyString(), any(), anyCollection()))
                .thenReturn(false);
        when(bookRepository.findByWorkSlug("frankenstein")).thenReturn(List.of(polish));
        when(gutenbergClient.find("Frankenstein", "Mary Shelley", Language.EN))
                .thenReturn(Optional.of(new GutenbergMatch(84, "Frankenstein", "Shelley, Mary", 1851)));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.ask(user, new BookRequestAsk("Frankenstein", "Mary Shelley", Language.EN, null));

        ArgumentCaptor<BookRequest> saved = ArgumentCaptor.forClass(BookRequest.class);
        verify(repository).save(saved.capture());
        assertThat(saved.getValue().getWorkSlug()).isEqualTo("frankenstein");
        assertThat(saved.getValue().getGutenbergId()).isEqualTo(84);
        assertThat(saved.getValue().getStatus()).isEqualTo(BookRequestStatus.OPEN);
    }

    @Test
    void theQueueGroupsAsksIntoWorksAndCountsDistinctAskers() {
        User first = user();
        User second = user();
        BookRequest a = request(first, "Dracula", "Bram Stoker", Language.EN, null, 345);
        BookRequest b = request(second, "dracula", "Bram  Stoker", Language.EN, null, null);
        BookRequest c = request(first, "Frankenstein", "Mary Shelley", Language.PL, "frankenstein", null);
        c.setCreatedAt(Instant.now().minus(3, ChronoUnit.DAYS));
        when(repository.findByStatusIn(BookRequestStatus.STANDING)).thenReturn(List.of(a, b, c));
        when(bookRepository.findByWorkSlug("frankenstein"))
                .thenReturn(List.of(
                        book("frankenstein-en-c1", "frankenstein", "Frankenstein", "Mary Shelley", Language.EN),
                        book("frankenstein-uk-b2", "frankenstein", "Франкенштейн", "Мері Шеллі", Language.UK)));

        BookRequestQueueDto queue = service.queue();

        assertThat(queue.open()).isEqualTo(2);
        assertThat(queue.askers()).isEqualTo(3);
        BookRequestRow dracula = queue.rows().getFirst();
        assertThat(dracula.title()).isEqualTo("Dracula");
        assertThat(dracula.askers()).isEqualTo(2);
        assertThat(dracula.publicDomain()).isEqualTo("gutenberg");
        assertThat(dracula.workSlug()).isNull();
        BookRequestRow frankenstein = queue.rows().get(1);
        assertThat(frankenstein.haveLanguages()).containsExactly(Language.EN, Language.UK);
        assertThat(frankenstein.publicDomain()).isEqualTo("yes");
        assertThat(frankenstein.editorialUrl())
                .startsWith("https://books.example.test/editions/new?title=Frankenstein")
                .contains("work=frankenstein");
    }

    @Test
    void publishingTheEditionSettlesEveryAskerOnceWhetherTheyNamedTheWorkOrOnlyTheTitle() {
        User asker = user();
        User other = user();
        BookRequest byWork = request(asker, "Dracula", "Bram Stoker", Language.EN, "dracula", 345);
        BookRequest byTitle = request(other, "Dracula", "bram stoker", Language.EN, null, null);
        Book published = book("dracula-en", "dracula", "Dracula", "Bram Stoker", Language.EN);
        when(repository.findByWorkSlugAndLanguageAndStatusIn("dracula", Language.EN, BookRequestStatus.STANDING))
                .thenReturn(List.of(byWork));
        when(repository.findByNormalizedKeyAndLanguageAndStatusIn(
                        "dracula|bram stoker", Language.EN, BookRequestStatus.STANDING))
                .thenReturn(List.of(byWork, byTitle));

        service.settlePublished(published);

        assertThat(byWork.getStatus()).isEqualTo(BookRequestStatus.PUBLISHED);
        assertThat(byTitle.getPublishedBook()).isSameAs(published);
        verify(bookEmailService, times(2)).requestPublished(any(), eq("Dracula"), anyString(), eq("/books/dracula-en"));
        verify(notificationService)
                .notifyOfRequestPublished(
                        eq(asker), eq(byWork.getId()), eq("Dracula"), anyString(), any(), eq("/books/dracula-en"));
    }

    @Test
    void decliningIsOneClickAndSendsNothing() {
        User operator = user();
        BookRequest open = request(user(), "Normal People", "Sally Rooney", Language.EN, null, null);
        when(repository.findById(open.getId())).thenReturn(Optional.of(open));
        when(repository.findByNormalizedKeyAndLanguageAndStatusIn(
                        eq("normal people|sally rooney"), eq(Language.EN), anyCollection()))
                .thenReturn(List.of(open));
        when(repository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        BookRequestRow row = service.decline(operator, open.getId());

        assertThat(row.status()).isEqualTo(BookRequestStatus.DECLINED);
        assertThat(row.publicDomain()).isEqualTo("unlikely");
        verify(bookEmailService, never()).requestPublished(any(), any(), any(), any());
    }

    private static User user() {
        User user = new User();
        user.setId(UUID.randomUUID());
        return user;
    }

    private static BookRequest request(
            User user, String title, String author, Language language, String workSlug, Integer gutenbergId) {
        BookRequest request = new BookRequest();
        request.setId(UUID.randomUUID());
        request.setUser(user);
        request.setTitle(title);
        request.setAuthor(author);
        request.setNormalizedKey(BookRequestService.normalizedKey(title, author));
        request.setLanguage(language);
        request.setWorkSlug(workSlug);
        request.setGutenbergId(gutenbergId);
        request.setStatus(BookRequestStatus.OPEN);
        request.setCreatedAt(Instant.now());
        return request;
    }

    private static Book book(String editionSlug, String workSlug, String title, String author, Language language) {
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setEditionSlug(editionSlug);
        book.setWorkSlug(workSlug);
        book.setTitle(title);
        book.setAuthor(author);
        book.setLanguage(language);
        book.setPublicationYear(1818);
        return book;
    }
}
