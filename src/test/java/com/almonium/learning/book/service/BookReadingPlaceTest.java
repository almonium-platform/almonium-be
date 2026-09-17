package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.LearnerBookProgress;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.book.repository.LearnerBookProgressRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** The chapter rides with the percentage and is checked against the book's count; a bare percentage leaves it alone. */
@ExtendWith(MockitoExtension.class)
class BookReadingPlaceTest {
    @Mock
    BookRepository bookRepository;

    @Mock
    LearnerBookProgressRepository progressRepository;

    @Mock
    LearnerFinder learnerFinder;

    @InjectMocks
    BookService service;

    User user;
    Book book;

    @BeforeEach
    void aBookAndAReader() {
        user = new User();
        user.setId(UUID.randomUUID());
        book = new Book();
        book.setId(UUID.randomUUID());
        book.setLanguage(Language.EN);
        book.setChapterCount(24);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
    }

    @Test
    void thePlaceIsSavedBesideThePercentage() {
        LearnerBookProgress progress = new LearnerBookProgress(new Learner(), book, 5);
        when(progressRepository.findByUserIdAndBookId(user.getId(), book.getId()))
                .thenReturn(Optional.of(progress));

        service.saveBookProgress(user, book.getId(), 12, 3);

        assertThat(progress.getProgressPercentage()).isEqualTo(12);
        assertThat(progress.getCurrentChapter()).isEqualTo(3);
        assertThat(progress.getLastReadAt()).isNotNull();
        verify(progressRepository).save(progress);
    }

    @Test
    void aBarePercentageKeepsTheLastKnownPlace() {
        LearnerBookProgress progress = new LearnerBookProgress(new Learner(), book, 12);
        progress.setCurrentChapter(3);
        when(progressRepository.findByUserIdAndBookId(user.getId(), book.getId()))
                .thenReturn(Optional.of(progress));

        service.saveBookProgress(user, book.getId(), 14);

        assertThat(progress.getProgressPercentage()).isEqualTo(14);
        assertThat(progress.getCurrentChapter()).isEqualTo(3);
    }

    @Test
    void anImpossiblePlaceIsRefusedBeforeAnythingIsWritten() {
        LearnerBookProgress progress = new LearnerBookProgress(new Learner(), book, 12);
        when(progressRepository.findByUserIdAndBookId(user.getId(), book.getId()))
                .thenReturn(Optional.of(progress));

        assertThatThrownBy(() -> service.saveBookProgress(user, book.getId(), 14, 30))
                .isInstanceOf(BadUserRequestActionException.class);
        assertThatThrownBy(() -> service.saveBookProgress(user, book.getId(), 14, 0))
                .isInstanceOf(BadUserRequestActionException.class);
        verify(progressRepository, never()).save(any());
    }

    @Test
    void aBookPublishedBeforeTheCountAcceptsAnyChapter() {
        book.setChapterCount(null);
        LearnerBookProgress progress = new LearnerBookProgress(new Learner(), book, 12);
        when(progressRepository.findByUserIdAndBookId(user.getId(), book.getId()))
                .thenReturn(Optional.of(progress));

        service.saveBookProgress(user, book.getId(), 14, 30);

        assertThat(progress.getCurrentChapter()).isEqualTo(30);
    }

    @Test
    void aFirstOpeningStartsTheRowWithItsPlace() {
        Learner learner = new Learner();
        when(progressRepository.findByUserIdAndBookId(user.getId(), book.getId()))
                .thenReturn(Optional.empty());
        when(learnerFinder.findLearner(user, Language.EN)).thenReturn(learner);

        service.saveBookProgress(user, book.getId(), 1, 1);

        ArgumentCaptor<LearnerBookProgress> saved = ArgumentCaptor.forClass(LearnerBookProgress.class);
        verify(progressRepository).save(saved.capture());
        assertThat(saved.getValue().getLearner()).isSameAs(learner);
        assertThat(saved.getValue().getProgressPercentage()).isEqualTo(1);
        assertThat(saved.getValue().getCurrentChapter()).isEqualTo(1);
    }
}
