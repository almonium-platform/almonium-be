package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.response.LibrarySuggestionDto;
import com.almonium.learning.book.dto.response.LibrarySuggestionQueueDto;
import com.almonium.learning.book.dto.response.ProcessorIngestStatus;
import com.almonium.learning.book.model.entity.Book;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LibrarySuggestionServiceTest {
    @Mock
    LibrarySuggestionRepository repository;

    @Mock
    UserBookImportRepository importRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookProcessorClient processorClient;

    @Mock
    EffectiveAccessService effectiveAccessService;

    @Mock
    BookEmailService bookEmailService;

    @InjectMocks
    LibrarySuggestionService service;

    @Test
    void aWorkTheLibraryAlreadyCarriesIsSettledOnTheSpot() {
        UserBookImport bookImport = readyImport("Der Schimmelreiter", "Theodor Storm");
        Book libraryCopy = new Book();
        libraryCopy.setId(UUID.randomUUID());
        libraryCopy.setEditionSlug("der-schimmelreiter-de-original");
        libraryCopy.setTitle("Der Schimmelreiter");
        libraryCopy.setAuthor("theodor storm");
        libraryCopy.setLanguage(Language.DE);
        when(importRepository.findByIdAndUserId(
                        bookImport.getId(), bookImport.getUser().getId()))
                .thenReturn(Optional.of(bookImport));
        when(repository.existsByBookImportIdAndStatusIn(bookImport.getId(), LibrarySuggestionStatus.OPEN))
                .thenReturn(false);
        when(bookRepository.findByWorkSlug("der-schimmelreiter")).thenReturn(List.of(libraryCopy));
        when(bookRepository.findAvailableLanguagesForBook(libraryCopy.getId())).thenReturn(List.of());
        when(repository.save(any(LibrarySuggestion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LibrarySuggestionDto result = service.suggest(bookImport.getUser(), bookImport.getId());

        assertThat(result.status()).isEqualTo(LibrarySuggestionStatus.PUBLISHED);
        assertThat(result.libraryEditionSlug()).isEqualTo("der-schimmelreiter-de-original");
        verify(processorClient, never()).startLibraryIngest(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void aNewWorkWaitsForAnOperator() {
        UserBookImport bookImport = readyImport("Unterleuten", "Juli Zeh");
        when(importRepository.findByIdAndUserId(
                        bookImport.getId(), bookImport.getUser().getId()))
                .thenReturn(Optional.of(bookImport));
        when(repository.existsByBookImportIdAndStatusIn(bookImport.getId(), LibrarySuggestionStatus.OPEN))
                .thenReturn(false);
        when(bookRepository.findByWorkSlug("unterleuten")).thenReturn(List.of());
        when(repository.save(any(LibrarySuggestion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LibrarySuggestionDto result = service.suggest(bookImport.getUser(), bookImport.getId());

        assertThat(result.status()).isEqualTo(LibrarySuggestionStatus.SUGGESTED);
        assertThat(result.libraryEditionSlug()).isNull();
    }

    @Test
    void anImportHoldsOneOpenSuggestionAtATime() {
        UserBookImport bookImport = readyImport("Unterleuten", "Juli Zeh");
        when(importRepository.findByIdAndUserId(
                        bookImport.getId(), bookImport.getUser().getId()))
                .thenReturn(Optional.of(bookImport));
        when(repository.existsByBookImportIdAndStatusIn(bookImport.getId(), LibrarySuggestionStatus.OPEN))
                .thenReturn(true);

        assertThatThrownBy(() -> service.suggest(bookImport.getUser(), bookImport.getId()))
                .isInstanceOf(ResourceConflictException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void unconfirmedDetailsCannotBeSuggested() {
        UserBookImport bookImport = readyImport("Unterleuten", "Juli Zeh");
        bookImport.setMetadataStatus(BookImportMetadataStatus.PROPOSED);
        when(importRepository.findByIdAndUserId(
                        bookImport.getId(), bookImport.getUser().getId()))
                .thenReturn(Optional.of(bookImport));

        assertThatThrownBy(() -> service.suggest(bookImport.getUser(), bookImport.getId()))
                .isInstanceOf(BadUserRequestActionException.class);
    }

    @Test
    void theQueueGroupsTheSameWorkAndFlagsItsAge() {
        LibrarySuggestion first =
                suggestion("Der Schimmelreiter", "Theodor Storm", 1888, LibrarySuggestionStatus.SUGGESTED);
        LibrarySuggestion second =
                suggestion("der schimmelreiter", "Theodor Storm ", 1888, LibrarySuggestionStatus.SUGGESTED);
        second.setCreatedAt(first.getCreatedAt().plusSeconds(60));
        LibrarySuggestion recent = suggestion("Unterleuten", "Juli Zeh", 2016, LibrarySuggestionStatus.SUGGESTED);
        when(repository.findByStatusIn(LibrarySuggestionStatus.OPEN)).thenReturn(List.of(second, recent, first));
        when(effectiveAccessService.entitlementsFor(anyCollection()))
                .thenReturn(Map.of(first.getUser().getId(), Entitlement.PREMIUM));
        when(bookRepository.findByWorkSlug(any())).thenReturn(List.of());
        when(repository.countByStatus(LibrarySuggestionStatus.PUBLISHED)).thenReturn(9L);
        when(repository.countByStatus(LibrarySuggestionStatus.DECLINED)).thenReturn(6L);

        LibrarySuggestionQueueDto queue = service.queue();

        assertThat(queue.open()).isEqualTo(2);
        assertThat(queue.published()).isEqualTo(9);
        assertThat(queue.rows()).hasSize(2);
        assertThat(queue.rows().getFirst().id()).isEqualTo(first.getId());
        assertThat(queue.rows().getFirst().imports()).isEqualTo(2);
        assertThat(queue.rows().getFirst().premiumCount()).isEqualTo(1);
        assertThat(queue.rows().getFirst().publicDomainHint()).isEqualTo("pd");
        assertThat(queue.rows().get(1).publicDomainHint()).isEqualTo("check");
    }

    @Test
    void acceptingPostsOneIngestForTheWholeGroup() {
        LibrarySuggestion first =
                suggestion("Der Schimmelreiter", "Theodor Storm", 1888, LibrarySuggestionStatus.SUGGESTED);
        LibrarySuggestion second =
                suggestion("Der Schimmelreiter", "Theodor Storm", 1888, LibrarySuggestionStatus.SUGGESTED);
        when(repository.findById(first.getId())).thenReturn(Optional.of(first));
        when(repository.findByStatusIn(List.of(LibrarySuggestionStatus.SUGGESTED)))
                .thenReturn(List.of(first, second));
        UUID editionId = UUID.randomUUID();
        when(processorClient.startLibraryIngest(
                        first.getId(),
                        first.getBookImport().getId(),
                        first.getUser().getId(),
                        "Der Schimmelreiter",
                        "Theodor Storm",
                        "",
                        Language.DE,
                        1888))
                .thenReturn(new ProcessorIngestStatus(
                        editionId, "der-schimmelreiter-de-original", "queued", "ingesting", 5, "", null, null));

        service.accept(operator(), first.getId());

        assertThat(first.getStatus()).isEqualTo(LibrarySuggestionStatus.INGESTING);
        assertThat(second.getStatus()).isEqualTo(LibrarySuggestionStatus.INGESTING);
        assertThat(second.getProcessorEditionId()).isEqualTo(editionId);
        assertThat(second.getPhase()).isEqualTo("ingesting");
        verify(repository).saveAll(List.of(first, second));
    }

    @Test
    void decliningTellsEverySuggesterPlainly() {
        LibrarySuggestion first = suggestion("Unterleuten", "Juli Zeh", 2016, LibrarySuggestionStatus.SUGGESTED);
        LibrarySuggestion second = suggestion("Unterleuten", "Juli Zeh", 2016, LibrarySuggestionStatus.SUGGESTED);
        when(repository.findById(first.getId())).thenReturn(Optional.of(first));
        when(repository.findByStatusIn(List.of(LibrarySuggestionStatus.SUGGESTED, LibrarySuggestionStatus.INGESTING)))
                .thenReturn(List.of(first, second));

        service.decline(operator(), first.getId());

        assertThat(first.getStatus()).isEqualTo(LibrarySuggestionStatus.DECLINED);
        assertThat(second.getStatus()).isEqualTo(LibrarySuggestionStatus.DECLINED);
        verify(bookEmailService).suggestionDeclined(List.of(first.getUser(), second.getUser()), "Unterleuten");
    }

    @Test
    void publicationNamingOurSuggestionSettlesTheGroupAndMails() {
        LibrarySuggestion first =
                suggestion("Der Schimmelreiter", "Theodor Storm", 1888, LibrarySuggestionStatus.INGESTING);
        LibrarySuggestion second =
                suggestion("Der Schimmelreiter", "Theodor Storm", 1888, LibrarySuggestionStatus.INGESTING);
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle("Der Schimmelreiter");
        book.setEditionSlug("der-schimmelreiter-de-original");
        when(repository.findById(first.getId())).thenReturn(Optional.of(first));
        when(repository.findByStatusIn(List.of(LibrarySuggestionStatus.INGESTING)))
                .thenReturn(List.of(first, second));

        service.settlePublished(first.getId(), book);

        assertThat(first.getStatus()).isEqualTo(LibrarySuggestionStatus.PUBLISHED);
        assertThat(second.getLibraryBook()).isEqualTo(book);
        assertThat(second.getPublishedAt()).isNotNull();
        verify(bookEmailService)
                .suggestionPublished(
                        List.of(first.getUser(), second.getUser()),
                        "Der Schimmelreiter",
                        "der-schimmelreiter-de-original");
    }

    @Test
    void anUnknownExternalJobIdIsIgnoredByPublication() {
        UUID unknown = UUID.randomUUID();
        when(repository.findById(unknown)).thenReturn(Optional.empty());

        service.settlePublished(unknown, new Book());

        verify(bookEmailService, never()).suggestionPublished(anyList(), any(), any());
    }

    @Test
    void slugsMatchTheProcessorsSlugify() {
        assertThat(Slugs.slugify("Der Schimmelreiter")).isEqualTo("der-schimmelreiter");
        assertThat(Slugs.slugify("  Effi   Briest! ")).isEqualTo("effi-briest");
        assertThat(Slugs.slugify("Крейцерова соната")).isEmpty();
        assertThat(Slugs.slugify("Émile, ou De l'éducation")).isEqualTo("emile-ou-de-leducation");
    }

    private static UserBookImport readyImport(String title, String author) {
        User user = new User();
        user.setId(UUID.randomUUID());
        UserBookImport bookImport = new UserBookImport();
        bookImport.setId(UUID.randomUUID());
        bookImport.setUser(user);
        bookImport.setTitle(title);
        bookImport.setAuthor(author);
        bookImport.setDescription("");
        bookImport.setLanguage(Language.DE);
        bookImport.setPublicationYear(1888);
        bookImport.setStatus(BookImportStatus.READY);
        bookImport.setMetadataStatus(BookImportMetadataStatus.CONFIRMED);
        return bookImport;
    }

    private static LibrarySuggestion suggestion(String title, String author, int year, LibrarySuggestionStatus status) {
        UserBookImport bookImport = readyImport(title, author);
        LibrarySuggestion suggestion = new LibrarySuggestion();
        suggestion.setId(UUID.randomUUID());
        suggestion.setBookImport(bookImport);
        suggestion.setUser(bookImport.getUser());
        suggestion.setTitle(title);
        suggestion.setAuthor(author);
        suggestion.setLanguage(Language.DE);
        suggestion.setPublicationYear(year);
        suggestion.setDescription("");
        suggestion.setStatus(status);
        suggestion.setError("");
        suggestion.setCreatedAt(Instant.now().minus(1, ChronoUnit.HOURS));
        return suggestion;
    }

    private static User operator() {
        User user = new User();
        user.setId(UUID.randomUUID());
        return user;
    }
}
