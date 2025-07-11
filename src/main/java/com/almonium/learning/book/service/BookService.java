package com.almonium.learning.book.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.infra.storage.service.FirebaseStorageService;
import com.almonium.learning.book.dto.response.BookDetails;
import com.almonium.learning.book.dto.response.BookDto;
import com.almonium.learning.book.dto.response.BookLanguageVariant;
import com.almonium.learning.book.dto.response.BookMiniDetails;
import com.almonium.learning.book.dto.response.BookshelfViewDto;
import com.almonium.learning.book.mapper.BookMapper;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.BookDetailsProjection;
import com.almonium.learning.book.model.entity.BookFavorite;
import com.almonium.learning.book.model.entity.BookMiniProjection;
import com.almonium.learning.book.model.entity.LearnerBookProgress;
import com.almonium.learning.book.model.entity.TranslationOrder;
import com.almonium.learning.book.repository.BookFavoriteRepository;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.book.repository.LearnerBookProgressRepository;
import com.almonium.learning.book.repository.TranslationOrderRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
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
    FirebaseStorageService firebaseStorageService;

    BookRepository bookRepository;
    UserRepository userRepository;
    TranslationOrderRepository translationOrderRepository;
    LearnerBookProgressRepository learnerBookProgressRepository;
    BookFavoriteRepository bookFavoriteRepository;

    BookMapper bookMapper;

    public List<BookDto> getBooks() {
        return bookMapper.toBookDtos(bookRepository.findAll());
    }

    public List<BookDto> getBooksInLanguage(Language language) {
        return bookMapper.toBookDtos(bookRepository.findByLanguage(language));
    }

    public void addToFavorites(User user, Long bookId, Language language) {
        Learner learner = learnerFinder.findLearner(user, language);
        Book book = getBookById(bookId);

        BookFavorite bookFavorite = new BookFavorite(learner, book);
        bookFavoriteRepository.save(bookFavorite);
    }

    public boolean deleteFromFavorites(User user, Long bookId, Language language) {
        Learner learner = learnerFinder.findLearner(user, language);
        return bookFavoriteRepository.deleteByLearnerIdAndBookId(learner.getId(), bookId) > 0;
    }

    public Book getBookById(Long bookId) {
        return bookRepository
                .findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
    }

    public BookshelfViewDto getBooksInLanguage(User user, Language language, Boolean includeTranslations) {
        UUID learnerId = learnerFinder.findLearner(user, language).getId();
        Set<Language> fluentLanguages = userRepository.findFluentLangsById(user.getId());

        List<BookDetailsProjection> booksInProgress =
                bookRepository.findBooksInProgressByLearner(learnerId, language, fluentLanguages);

        List<BookDetailsProjection> availableBooks =
                bookRepository.findAvailableBooks(language, learnerId, fluentLanguages, includeTranslations);

        List<BookDetailsProjection> favoriteBooks =
                bookRepository.findFavoriteBooks(language, learnerId, fluentLanguages, includeTranslations);

        return new BookshelfViewDto(
                convertToBookDtos(booksInProgress),
                convertToBookDtos(availableBooks),
                convertToBookDtos(favoriteBooks));
    }

    private List<BookDto> convertToBookDtos(List<BookDetailsProjection> books) {
        return bookMapper.toDto(books);
    }

    public boolean deleteBookProgress(User user, Long bookId) {
        int deletedCount = learnerBookProgressRepository.deleteByUserIdAndBookId(user.getId(), bookId);
        return deletedCount > 0;
    }

    public void saveBookProgress(User user, Long bookId, int progressPercentage) {
        Optional<LearnerBookProgress> progressOptional =
                learnerBookProgressRepository.findByUserIdAndBookId(user.getId(), bookId);

        if (progressOptional.isPresent()) {
            LearnerBookProgress progress = progressOptional.get();
            progress.setProgressPercentage(progressPercentage);
            progress.setLastReadAt(Instant.now());
            learnerBookProgressRepository.save(progress);
            log.debug("Updated progress for user {} and book {}", user.getId(), bookId);
        } else {
            log.debug("No existing progress found for user {} and book {}. Creating new record.", user.getId(), bookId);

            Book book = getBookById(bookId);
            Learner learner = learnerFinder.findLearner(user, book.getLanguage());

            LearnerBookProgress newProgress = new LearnerBookProgress(learner, book, progressPercentage);
            newProgress.setLastReadAt(Instant.now());
            learnerBookProgressRepository.save(newProgress);
        }
    }

    // by other services
    public List<Language> getAvailableLanguagesForBook(Long bookId) {
        return bookRepository.findAvailableLanguagesForBook(bookId).stream()
                .map(BookMiniProjection::getLanguage)
                .toList();
    }

    public BookMiniDetails getBookById(UUID userId, Long bookId) {
        List<BookLanguageVariant> languageVariants =
                bookMapper.toMiniDto(bookRepository.findAvailableLanguagesForBook(bookId));

        int progressPercentage = learnerBookProgressRepository
                .findByUserIdAndBookId(userId, bookId)
                .map(LearnerBookProgress::getProgressPercentage)
                .orElse(0);

        Language language = getBookById(bookId).getLanguage();

        return BookMiniDetails.builder()
                .languageVariants(languageVariants)
                .language(language)
                .progressPercentage(progressPercentage)
                .build();
    }

    public BookDetails getBookById(User user, Language language, Long bookId) {
        UUID learnerId = learnerFinder.findLearner(user, language).getId();
        Set<Language> fluentLanguages = userRepository.findFluentLangsById(user.getId());

        BookDetailsProjection projection = bookRepository
                .findBookDtoById(bookId, learnerId, fluentLanguages)
                .orElseThrow(EntityNotFoundException::new);

        List<BookLanguageVariant> availableLanguages =
                bookMapper.toMiniDto(bookRepository.findAvailableLanguagesForBook(bookId));
        Long originalBookId = projection.getOriginalId() == null ? bookId : projection.getOriginalId();
        Optional<TranslationOrder> order =
                translationOrderRepository.findByUserIdAndBookId(user.getId(), originalBookId);
        Optional<BookFavorite> favorite = bookFavoriteRepository.findByLearnerIdAndBookId(learnerId, bookId);

        return bookMapper.toDetailsDto(projection, availableLanguages, order, favorite);
    }

    public byte[] getText(User user, Long bookId) {
        return firebaseStorageService.getBook(bookId);
    }

    public byte[] getParallelBook(User user, Language language, Long bookId) {
        List<BookLanguageVariant> availableLanguages =
                bookMapper.toMiniDto(bookRepository.findAvailableLanguagesForBook(bookId));

        BookLanguageVariant miniDetails = availableLanguages.stream()
                .filter(book -> book.getLanguage().equals(language))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Book not found in this language"));

        Long secondId = miniDetails.getId();

        if (bookId.equals(secondId)) {
            throw new BadUserRequestActionException("This book is already in this language");
        }

        return firebaseStorageService.getParallelText(bookId, secondId);
    }
}
