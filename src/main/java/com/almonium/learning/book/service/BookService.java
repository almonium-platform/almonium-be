package com.almonium.learning.book.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.learning.book.dto.response.BookDto;
import com.almonium.learning.book.dto.response.BookshelfViewDto;
import com.almonium.learning.book.mapper.BookMapper;
import com.almonium.learning.book.model.entity.BookWithTranslationStatus;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.book.repository.LearnerBookProgressRepository;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional
public class BookService {
    LearnerFinder learnerFinder;

    BookRepository bookRepository;
    LearnerBookProgressRepository learnerBookProgressRepository;

    BookMapper bookMapper;
    private final UserRepository userRepository;

    public List<BookDto> getBooks() {
        return bookMapper.toBookDtos(bookRepository.findAll());
    }

    public List<BookDto> getBooksInLanguage(Language language) {
        return bookMapper.toBookDtos(bookRepository.findByLanguage(language));
    }

    public BookshelfViewDto getBooksInLanguage(User user, Language language, Boolean includeTranslations) {
        UUID learnerId = learnerFinder.findLearner(user, language).getId();
        Set<Language> fluentLanguages = userRepository.findFluentLangsById(user.getId());

        List<BookWithTranslationStatus> booksInProgress =
                bookRepository.findBooksInProgressByLearner(learnerId, language, fluentLanguages);

        List<BookWithTranslationStatus> availableBooks =
                bookRepository.findAvailableBooks(language, learnerId, fluentLanguages, includeTranslations);

        return new BookshelfViewDto(convertToBookDtos(booksInProgress), convertToBookDtos(availableBooks));
    }

    private List<BookDto> convertToBookDtos(List<BookWithTranslationStatus> books) {
        return books.stream()
                .map(book -> new BookDto(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublicationYear(),
                        book.getCoverImageUrl(),
                        book.getWordCount(),
                        book.getRating(),
                        book.getLanguage(),
                        book.getLevelFrom(),
                        book.getLevelTo(),
                        book.getProgressPercentage(),
                        book.getHasTranslation(),
                        book.getHasParallelTranslation(),
                        book.getIsTranslation()))
                .toList();
    }

    public void deleteBookProgress(User user, Language language, Long bookId) {
        UUID learnerId = learnerFinder.findLearner(user, language).getId();
        learnerBookProgressRepository.deleteByLearnerIdAndBookId(learnerId, bookId);
    }
}
