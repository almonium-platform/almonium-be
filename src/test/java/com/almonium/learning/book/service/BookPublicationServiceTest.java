package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.request.BookPublicationRequest;
import com.almonium.learning.book.dto.request.BookWithdrawalRequest;
import com.almonium.learning.book.dto.response.BookWithdrawalResponse;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookPublicationServiceTest {
    @Mock
    BookRepository bookRepository;

    @Mock
    TranslationOrderService translationOrderService;

    @Mock
    TranslationJobService translationJobService;

    @Mock
    LibrarySuggestionService librarySuggestionService;

    @Mock
    BookRequestService bookRequestService;

    @InjectMocks
    BookPublicationService service;

    @Test
    void storesTheProcessorProjectionWithoutInventedCatalogValues() {
        BookPublicationRequest request = new BookPublicationRequest(
                "frankenstein-en",
                "a".repeat(64),
                "frankenstein",
                "Frankenstein",
                "Mary Shelley",
                "A scientist creates life.",
                Language.EN,
                Language.EN,
                "original",
                null,
                null,
                1818,
                null,
                CEFR.C1,
                75000,
                null,
                null,
                null,
                null,
                null,
                null);
        when(bookRepository.findAnyByEditionSlug(request.editionSlug())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.publish(request);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(captor.capture());
        Book book = captor.getValue();
        assertThat(book.getSourceHash()).isEqualTo("a".repeat(64));
        assertThat(book.getPublicationYear()).isEqualTo(1818);
        assertThat(book.getDescription()).isEqualTo("A scientist creates life.");
        assertThat(book.getCefrLevel()).isEqualTo(CEFR.C1);
        assertThat(book.getCoverUrl()).isNull();
    }

    @Test
    void publishingATranslationSettlesTheRequestsAndTheJob() {
        Book original = new Book();
        original.setId(UUID.randomUUID());
        original.setEditionSlug("effi-briest-de-original");
        when(bookRepository.findAnyByEditionSlug("effi-briest-uk-parallel")).thenReturn(Optional.empty());
        when(bookRepository.findByEditionSlug("effi-briest-de-original")).thenReturn(Optional.of(original));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.publish(new BookPublicationRequest(
                "effi-briest-uk-parallel",
                "b".repeat(64),
                "effi-briest",
                "Effi Briest",
                "Theodor Fontane",
                "",
                Language.DE,
                Language.UK,
                "machine_translation",
                "effi-briest-de-original",
                "AI (contemporary neutral)",
                1895,
                null,
                CEFR.B2,
                80000,
                UUID.randomUUID(),
                null,
                null,
                null,
                null,
                null));

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(translationOrderService).publishTranslation(captor.capture());
        verify(translationJobService).settlePublished(captor.getValue());
        assertThat(captor.getValue().getOriginalBook()).isEqualTo(original);
        verify(librarySuggestionService, never()).settlePublished(any(), any());
    }

    @Test
    void publishingAnIngestedSuggestionSettlesTheSuggestionGroup() {
        UUID suggestionId = UUID.randomUUID();
        when(bookRepository.findAnyByEditionSlug("der-schimmelreiter-de-original"))
                .thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.publish(new BookPublicationRequest(
                "der-schimmelreiter-de-original",
                "c".repeat(64),
                "der-schimmelreiter",
                "Der Schimmelreiter",
                "Theodor Storm",
                "",
                Language.DE,
                Language.DE,
                "original",
                null,
                null,
                1888,
                null,
                CEFR.B2,
                41000,
                UUID.randomUUID(),
                suggestionId,
                null,
                null,
                null,
                null));

        verify(librarySuggestionService).settlePublished(eq(suggestionId), any(Book.class));
        verify(translationOrderService, never()).publishTranslation(any());
    }

    @Test
    void withdrawalStopsServingTheBookButKeepsTheRowReadersReferTo() {
        Book book = new Book();
        book.setEditionSlug("frankenstein-en");
        when(bookRepository.findAnyByEditionSlug("frankenstein-en")).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookWithdrawalResponse response = service.withdraw(new BookWithdrawalRequest("frankenstein-en"));

        assertThat(response.withdrawn()).isTrue();
        assertThat(book.getWithdrawnAt()).isNotNull();
        verify(bookRepository).save(book);
    }

    @Test
    void withdrawalIsRefusedWhilePublishedTranslationsPointAtTheBook() {
        Book book = new Book();
        book.setEditionSlug("frankenstein-en");
        when(bookRepository.findAnyByEditionSlug("frankenstein-en")).thenReturn(Optional.of(book));
        when(bookRepository.countTranslationsOf(book.getId())).thenReturn(1L);

        assertThatThrownBy(() -> service.withdraw(new BookWithdrawalRequest("frankenstein-en")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("translation");
        assertThat(book.getWithdrawnAt()).isNull();
    }

    @Test
    void withdrawingTwiceReportsNothingLeftToDoSoARetriedTakedownCanFinish() {
        Book book = new Book();
        book.setEditionSlug("frankenstein-en");
        book.setWithdrawnAt(Instant.now());
        when(bookRepository.findAnyByEditionSlug("frankenstein-en")).thenReturn(Optional.of(book));

        BookWithdrawalResponse response = service.withdraw(new BookWithdrawalRequest("frankenstein-en"));

        assertThat(response.withdrawn()).isFalse();
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void withdrawingAnEditionWeNeverPublishedIsNotAnError() {
        when(bookRepository.findAnyByEditionSlug("unknown-en")).thenReturn(Optional.empty());

        BookWithdrawalResponse response = service.withdraw(new BookWithdrawalRequest("unknown-en"));

        assertThat(response.withdrawn()).isFalse();
        assertThat(response.bookId()).isNull();
    }

    @Test
    void publishingAgainRevivesAWithdrawnBookInsteadOfDuplicatingIt() {
        Book withdrawn = new Book();
        withdrawn.setEditionSlug("frankenstein-en");
        withdrawn.setWithdrawnAt(Instant.now());
        BookPublicationRequest request = new BookPublicationRequest(
                "frankenstein-en",
                "a".repeat(64),
                "frankenstein",
                "Frankenstein",
                "Mary Shelley",
                "A scientist creates life.",
                Language.EN,
                Language.EN,
                "original",
                null,
                null,
                1818,
                null,
                CEFR.C1,
                75000,
                null,
                null,
                null,
                null,
                null,
                null);
        when(bookRepository.findAnyByEditionSlug("frankenstein-en")).thenReturn(Optional.of(withdrawn));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.publish(request);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(captor.capture());
        assertThat(captor.getValue()).isSameAs(withdrawn);
        assertThat(captor.getValue().getWithdrawnAt()).isNull();
    }
}
