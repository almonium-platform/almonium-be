package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.repository.LearningItemRepository;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.learning.book.dto.response.BookCertificateDto;
import com.almonium.learning.book.dto.response.BookChapter;
import com.almonium.learning.book.dto.response.ChapterVocabulary;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.BookCertificate;
import com.almonium.learning.book.repository.BookCertificateRepository;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/** Issued once at the end, frozen but for the saved count, public by default only once the reader has a name. */
@ExtendWith(MockitoExtension.class)
class BookCertificateServiceTest {
    @Mock
    BookCertificateRepository certificates;

    @Mock
    BookService bookService;

    @Mock
    PublishedBookContentService content;

    @Mock
    LearnerFinder learnerFinder;

    @Mock
    LearningItemRepository learningItems;

    @Spy
    RarestWordsSelector selector = new RarestWordsSelector();

    @InjectMocks
    BookCertificateService service;

    User user;
    Book book;
    Learner learner;

    @BeforeEach
    void aReaderAndABook() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("marta");
        user.setEmail("marta.k@example.com");
        book = new Book();
        book.setId(UUID.randomUUID());
        book.setEditionSlug("winnie-the-pooh");
        book.setTitle("Winnie-the-Pooh");
        book.setAuthor("A. A. Milne");
        book.setLanguage(Language.EN);
        book.setWordCount(24610);
        book.setChapterCount(2);
        learner = new Learner();
        lenient().when(certificates.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void theEndOfTheBookIssuesTheCertificateAndLandsProgressAtTheEnd() {
        when(bookService.getBookById(book.getId())).thenReturn(book);
        when(certificates.findByUserIdAndBookId(user.getId(), book.getId())).thenReturn(Optional.empty());
        when(content.chaptersFor(book)).thenReturn(List.of(chapter(1), chapter(2)));
        when(content.vocabularyFor(book, 1)).thenReturn(vocabulary(1, word("heffalump", "Heffalump", "rare")));
        when(content.vocabularyFor(book, 2)).thenThrow(new IllegalStateException("not ready"));
        when(learnerFinder.findLearner(user, Language.EN)).thenReturn(learner);
        when(learningItems.countByOwnerAndSourceBookId(learner, book.getId())).thenReturn(187L);

        BookCertificateDto issued = service.issue(user, book.getId());

        verify(bookService).saveBookProgress(user, book.getId(), 100, 2);
        ArgumentCaptor<BookCertificate> saved = ArgumentCaptor.forClass(BookCertificate.class);
        verify(certificates).save(saved.capture());
        assertThat(saved.getValue().getFinishedAt()).isNotNull();
        assertThat(issued.username()).isEqualTo("marta");
        assertThat(issued.words()).containsExactly("Heffalump");
        assertThat(issued.wordsRead()).isEqualTo(24610);
        assertThat(issued.wordsSaved()).isEqualTo(187);
        assertThat(issued.publicPage()).isTrue();
    }

    @Test
    void aSecondVisitToTheEndReturnsTheSameCertificateWithoutTouchingProgress() {
        BookCertificate existing = new BookCertificate(user, book, java.time.Instant.EPOCH, true, List.of("Heffalump"));
        when(bookService.getBookById(book.getId())).thenReturn(book);
        when(certificates.findByUserIdAndBookId(user.getId(), book.getId())).thenReturn(Optional.of(existing));
        when(learnerFinder.findLearner(user, Language.EN)).thenReturn(learner);
        when(learningItems.countByOwnerAndSourceBookId(learner, book.getId())).thenReturn(190L);

        BookCertificateDto again = service.issue(user, book.getId());

        verify(bookService, never()).saveBookProgress(any(), any(), anyInt(), any());
        verify(content, never()).chaptersFor(any());
        assertThat(again.finishedAt()).isEqualTo(java.time.Instant.EPOCH);
        assertThat(again.wordsSaved()).isEqualTo(190);
    }

    @Test
    void aGeneratedUsernameKeepsThePageOffUntilTheReaderTurnsItOn() {
        user.setUsername("martak7");
        when(bookService.getBookById(book.getId())).thenReturn(book);
        when(certificates.findByUserIdAndBookId(user.getId(), book.getId())).thenReturn(Optional.empty());
        when(content.chaptersFor(book)).thenReturn(List.of());
        when(learnerFinder.findLearner(user, Language.EN)).thenReturn(learner);

        assertThat(service.issue(user, book.getId()).publicPage()).isFalse();
    }

    @Test
    void thePublicPageIsNotFoundWhileItIsOff() {
        when(certificates.findPublic("marta", "winnie-the-pooh")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.publicView("Marta", "winnie-the-pooh"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void theSwitchIsTheOnlyThingTheOwnerChanges() {
        BookCertificate existing = new BookCertificate(user, book, java.time.Instant.EPOCH, true, List.of());
        when(certificates.findByUserIdAndBookId(user.getId(), book.getId())).thenReturn(Optional.of(existing));

        assertThat(service.setPublicPage(user, book.getId(), false).publicPage())
                .isFalse();
        assertThat(existing.isPublicPage()).isFalse();
    }

    @Test
    void theSignUpRuleReadBackwardsTellsAGeneratedNameFromAChosenOne() {
        assertThat(BookCertificateService.usernameLooksGenerated(user("marta", "marta@x.io")))
                .isTrue();
        assertThat(BookCertificateService.usernameLooksGenerated(user("martak42", "marta.k@x.io")))
                .isTrue();
        assertThat(BookCertificateService.usernameLooksGenerated(user("12345678901234567890", "a@x.io")))
                .isTrue();
        assertThat(BookCertificateService.usernameLooksGenerated(user("bookworm", "marta@x.io")))
                .isFalse();
        assertThat(BookCertificateService.usernameLooksGenerated(user("marta_reads", "marta@x.io")))
                .isFalse();
    }

    private static User user(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        return user;
    }

    private static BookChapter chapter(int sequence) {
        return new BookChapter(UUID.randomUUID(), sequence, "Chapter " + sequence, "complete", "B1", List.of());
    }

    private static ChapterVocabulary vocabulary(int sequence, ChapterVocabulary.Word... words) {
        return new ChapterVocabulary(UUID.randomUUID(), sequence, "en", "ready", "curated", List.of(words), null);
    }

    private static ChapterVocabulary.Word word(String lemma, String surface, String band) {
        return new ChapterVocabulary.Word(lemma, surface, "context", "c1.p1", 0, 1, band);
    }
}
