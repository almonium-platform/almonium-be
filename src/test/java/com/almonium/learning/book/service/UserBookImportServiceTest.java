package com.almonium.learning.book.service;

import static com.almonium.subscription.model.entity.enums.PlanFeature.MAX_BOOK_IMPORTS_ON_SHELF;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.learning.book.dto.request.BookImportEventRequest;
import com.almonium.learning.book.dto.request.BookImportMetadataRequest;
import com.almonium.learning.book.dto.response.BookImportDto;
import com.almonium.learning.book.dto.response.BookImportQuotaDto;
import com.almonium.learning.book.model.entity.UserBookImport;
import com.almonium.learning.book.model.enums.BookImportMetadataStatus;
import com.almonium.learning.book.model.enums.BookImportStatus;
import com.almonium.learning.book.repository.UserBookImportRepository;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class UserBookImportServiceTest {
    @Mock
    UserBookImportRepository repository;

    @Mock
    PlanValidationService planValidationService;

    @Mock
    BookProcessorClient processorClient;

    @Mock
    PublishedBookContentService contentService;

    @Mock
    NotificationService notificationService;

    @Mock
    LibrarySuggestionService librarySuggestionService;

    @InjectMocks
    UserBookImportService service;

    @Test
    void enforcesTheShelfCapAndQueuesProcessorImport() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("private-reader");
        when(repository.countByUserId(user.getId())).thenReturn(9L);
        when(planValidationService.effectiveLimit(user, MAX_BOOK_IMPORTS_ON_SHELF))
                .thenReturn(10);
        when(repository.save(any(UserBookImport.class))).thenAnswer(invocation -> invocation.getArgument(0));
        MockMultipartFile source =
                new MockMultipartFile("file", "book.epub", "application/epub+zip", new byte[] {1, 2, 3});

        BookImportDto result = service.create(user, source, "My Book", "An Author", "Private", Language.EN, 1920);

        verify(planValidationService).validatePlanFeature(user, MAX_BOOK_IMPORTS_ON_SHELF, 10);
        verify(processorClient)
                .createPrivateImport(
                        eq(result.id()),
                        eq(user.getId()),
                        eq("private-reader"),
                        eq(source),
                        eq("My Book"),
                        eq("An Author"),
                        eq("Private"),
                        eq(Language.EN),
                        eq(1920));
        assertThat(result.status().name()).isEqualTo("QUEUED");
    }

    @Test
    void importsWithoutDetailsUseTheFileNameUntilTheProcessorDetectsMetadata() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("private-reader");
        when(repository.countByUserId(user.getId())).thenReturn(0L);
        when(planValidationService.effectiveLimit(user, MAX_BOOK_IMPORTS_ON_SHELF))
                .thenReturn(10);
        when(repository.save(any(UserBookImport.class))).thenAnswer(invocation -> invocation.getArgument(0));
        MockMultipartFile source =
                new MockMultipartFile("file", "pride_and-prejudice.epub", "application/epub+zip", new byte[] {1, 2, 3});

        BookImportDto result = service.create(user, source, " ", null, null, null, null);

        assertThat(result.title()).isEqualTo("pride and prejudice");
        assertThat(result.author()).isEmpty();
        assertThat(result.language()).isNull();
        assertThat(result.metadataStatus()).isEqualTo(BookImportMetadataStatus.PENDING);
        verify(processorClient)
                .createPrivateImport(
                        eq(result.id()),
                        eq(user.getId()),
                        eq("private-reader"),
                        eq(source),
                        isNull(),
                        isNull(),
                        eq(""),
                        isNull(),
                        isNull());
    }

    @Test
    void adoptsDetectedMetadataUntilTheOwnerConfirms() {
        UserBookImport bookImport = pendingImport();
        when(repository.findByIdAndUserId(
                        bookImport.getId(), bookImport.getUser().getId()))
                .thenReturn(Optional.of(bookImport));
        when(repository.save(any(UserBookImport.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Map<String, String> provenance = Map.of("title", "source", "author", "ai", "language", "source");

        service.applyEvent(event(bookImport, "processing", 40, metadata("Pride and Prejudice", true, provenance)));

        assertThat(bookImport.getTitle()).isEqualTo("Pride and Prejudice");
        assertThat(bookImport.getAuthor()).isEqualTo("Jane Austen");
        assertThat(bookImport.getLanguage()).isEqualTo(Language.EN);
        assertThat(bookImport.getPublicationYear()).isEqualTo(1813);
        assertThat(bookImport.getMetadataStatus()).isEqualTo(BookImportMetadataStatus.PROPOSED);
        assertThat(bookImport.getMetadataProvenance()).isEqualTo(provenance);

        bookImport.setMetadataStatus(BookImportMetadataStatus.CONFIRMED);
        service.applyEvent(event(bookImport, "ready", 100, metadata("Renamed by a later run", true, provenance)));

        assertThat(bookImport.getTitle()).isEqualTo("Pride and Prejudice");
        assertThat(bookImport.getStatus()).isEqualTo(BookImportStatus.READY);
        verify(notificationService)
                .notifyOfBookImport(bookImport.getUser(), bookImport.getId(), "Pride and Prejudice", true);
    }

    @Test
    void ignoresMetadataTheProcessorHasNotDetectedYet() {
        UserBookImport bookImport = pendingImport();
        when(repository.findByIdAndUserId(
                        bookImport.getId(), bookImport.getUser().getId()))
                .thenReturn(Optional.of(bookImport));
        when(repository.save(any(UserBookImport.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.applyEvent(event(bookImport, "processing", 5, metadata("", false, Map.of())));

        assertThat(bookImport.getTitle()).isEqualTo("book");
        assertThat(bookImport.getMetadataStatus()).isEqualTo(BookImportMetadataStatus.PENDING);
    }

    @Test
    void confirmingDetailsRecordsTheOwnerAsSourceAndUpdatesTheProcessor() {
        UserBookImport bookImport = pendingImport();
        bookImport.setTitle("Pride and Prejudice");
        bookImport.setAuthor("Jane Austen");
        bookImport.setLanguage(Language.EN);
        bookImport.setMetadataStatus(BookImportMetadataStatus.PROPOSED);
        bookImport.setMetadataProvenance(Map.of("title", "source", "author", "ai", "language", "source"));
        when(repository.findByIdAndUserId(
                        bookImport.getId(), bookImport.getUser().getId()))
                .thenReturn(Optional.of(bookImport));
        when(repository.save(any(UserBookImport.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookImportDto result = service.confirmMetadata(
                bookImport.getUser(),
                bookImport.getId(),
                new BookImportMetadataRequest("Pride and Prejudice", "Jane Austen", " A novel. ", Language.DE, 1813));

        assertThat(result.metadataStatus()).isEqualTo(BookImportMetadataStatus.CONFIRMED);
        assertThat(result.metadataProvenance())
                .containsEntry("title", "source")
                .containsEntry("author", "ai")
                .containsEntry("language", "user")
                .containsEntry("description", "user")
                .containsEntry("publication_year", "user");
        assertThat(result.description()).isEqualTo("A novel.");
        verify(processorClient)
                .updatePrivateImportMetadata(
                        bookImport.getId(),
                        bookImport.getUser().getId(),
                        "Pride and Prejudice",
                        "Jane Austen",
                        "A novel.",
                        Language.DE,
                        1813);
    }

    @Test
    void replacingTheFileKeepsTheRowSoTheAllowanceIsNotSpentAgain() {
        UserBookImport bookImport = pendingImport();
        bookImport.setStatus(BookImportStatus.READY);
        bookImport.setTitle("Pride and Prejudice");
        bookImport.setAuthor("Jane Austen");
        bookImport.setLanguage(Language.EN);
        bookImport.setPublicationYear(1813);
        bookImport.setMetadataStatus(BookImportMetadataStatus.CONFIRMED);
        bookImport.setWordCount(1200);
        bookImport.getUser().setUsername("private-reader");
        when(repository.findByIdAndUserId(
                        bookImport.getId(), bookImport.getUser().getId()))
                .thenReturn(Optional.of(bookImport));
        when(repository.save(any(UserBookImport.class))).thenAnswer(invocation -> invocation.getArgument(0));
        MockMultipartFile source =
                new MockMultipartFile("file", "better-scan.epub", "application/epub+zip", new byte[] {4, 5, 6});

        BookImportDto result = service.replaceFile(bookImport.getUser(), bookImport.getId(), source);

        verify(processorClient)
                .deletePrivateImport(bookImport.getId(), bookImport.getUser().getId());
        verify(processorClient)
                .createPrivateImport(
                        bookImport.getId(),
                        bookImport.getUser().getId(),
                        "private-reader",
                        source,
                        "Pride and Prejudice",
                        "Jane Austen",
                        "",
                        Language.EN,
                        1813);
        verify(planValidationService, never()).validatePlanFeature(any(), any(), anyInt());
        assertThat(result.id()).isEqualTo(bookImport.getId());
        assertThat(result.status()).isEqualTo(BookImportStatus.QUEUED);
        assertThat(result.wordCount()).isZero();
        assertThat(result.metadataStatus()).isEqualTo(BookImportMetadataStatus.CONFIRMED);
    }

    @Test
    void replacingTheFileWaitsForARunningImport() {
        UserBookImport bookImport = pendingImport();
        bookImport.setStatus(BookImportStatus.PROCESSING);
        when(repository.findByIdAndUserId(
                        bookImport.getId(), bookImport.getUser().getId()))
                .thenReturn(Optional.of(bookImport));

        assertThatThrownBy(() -> service.replaceFile(
                        bookImport.getUser(),
                        bookImport.getId(),
                        new MockMultipartFile("file", "x.epub", "application/epub+zip", new byte[] {1})))
                .isInstanceOf(ResourceConflictException.class);
        verify(processorClient, never()).deletePrivateImport(any(), any());
    }

    @Test
    void deletingForgetsTheProcessorCopyFirst() {
        UserBookImport bookImport = pendingImport();
        when(repository.findByIdAndUserId(
                        bookImport.getId(), bookImport.getUser().getId()))
                .thenReturn(Optional.of(bookImport));

        service.delete(bookImport.getUser(), bookImport.getId());

        verify(processorClient)
                .deletePrivateImport(bookImport.getId(), bookImport.getUser().getId());
        verify(repository).delete(bookImport);
    }

    @Test
    void reportsThePlacesTakenOnTheShelfWhateverTheirAge() {
        User user = new User();
        user.setId(UUID.randomUUID());
        when(repository.countByUserId(user.getId())).thenReturn(7L);
        when(planValidationService.effectiveLimit(user, MAX_BOOK_IMPORTS_ON_SHELF))
                .thenReturn(10);

        BookImportQuotaDto quota = service.quota(user);

        assertThat(quota.limit()).isEqualTo(10);
        assertThat(quota.used()).isEqualTo(7);
    }

    private static UserBookImport pendingImport() {
        User user = new User();
        user.setId(UUID.randomUUID());
        UserBookImport bookImport = new UserBookImport();
        bookImport.setId(UUID.randomUUID());
        bookImport.setUser(user);
        bookImport.setTitle("book");
        bookImport.setAuthor("");
        bookImport.setDescription("");
        bookImport.setStatus(BookImportStatus.QUEUED);
        bookImport.setMetadataStatus(BookImportMetadataStatus.PENDING);
        bookImport.setMetadataProvenance(Map.of());
        bookImport.setError("");
        return bookImport;
    }

    private static BookImportEventRequest event(
            UserBookImport bookImport, String status, int progress, BookImportEventRequest.Metadata metadata) {
        return new BookImportEventRequest(
                bookImport.getId(),
                bookImport.getUser().getId(),
                status,
                metadata.title(),
                progress,
                120,
                null,
                metadata);
    }

    private static BookImportEventRequest.Metadata metadata(
            String title, boolean detected, Map<String, String> provenance) {
        return new BookImportEventRequest.Metadata(
                title, "Jane Austen", "A novel of manners.", "en", 1813, provenance, detected);
    }
}
