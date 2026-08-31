package com.almonium.learning.book.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.service.LearnerFinder;
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
import com.almonium.learning.book.repository.BookFavoriteRepository;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.book.repository.LearnerBookProgressRepository;
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
    PublishedBookContentService publishedBookContentService;

    BookRepository bookRepository;
    UserRepository userRepository;
    LearnerBookProgressRepository learnerBookProgressRepository;
    BookFavoriteRepository bookFavoriteRepository;

    BookMapper bookMapper;

    public List<BookDto> getBooks() {
        return bookRepository.findAll().stream().map(this::toPublicBookDto).toList();
    }

    public List<BookDto> getBooksInLanguage(Language language) {
        return bookRepository.findByLanguage(language).stream()
                .map(this::toPublicBookDto)
                .toList();
    }

    private BookDto toPublicBookDto(Book book) {
        boolean hasVariant =
                bookRepository.findAvailableLanguagesForBook(book.getId()).size() > 1;
        return new BookDto(
                book.getId(),
                book.getEditionSlug(),
                book.getWorkSlug(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription(),
                book.getPublicationYear(),
                book.getCoverUrl(),
                book.getWordCount(),
                book.getLanguage(),
                book.getCefrLevel(),
                null,
                hasVariant,
                hasVariant,
                book.getOriginalBook() != null);
    }

    public BookDetails getPublicBook(String editionSlug) {
        Book book = getBookBySlug(editionSlug);
        List<BookLanguageVariant> variants =
                bookMapper.toMiniDto(bookRepository.findAvailableLanguagesForBook(book.getId()));
        BookDetails details = new BookDetails();
        details.setId(book.getId());
        details.setEditionSlug(book.getEditionSlug());
        details.setWorkSlug(book.getWorkSlug());
        details.setTitle(book.getTitle());
        details.setAuthor(book.getAuthor());
        details.setDescription(book.getDescription());
        details.setPublicationYear(book.getPublicationYear());
        details.setCoverUrl(book.getCoverUrl());
        details.setWordCount(book.getWordCount());
        details.setLanguage(book.getLanguage());
        details.setCefrLevel(book.getCefrLevel());
        details.setIsTranslation(book.getOriginalBook() != null);
        details.setHasTranslation(variants.size() > 1);
        details.setHasParallelTranslation(variants.size() > 1);
        details.setLanguageVariants(variants);
        details.setOriginalLanguage(book.getOriginalLanguage());
        details.setOriginalId(
                book.getOriginalBook() == null
                        ? book.getId()
                        : book.getOriginalBook().getId());
        details.setTranslator(book.getTranslator());
        return details;
    }

    public byte[] getPublicText(String editionSlug) {
        return publishedBookContentService.textFor(getBookBySlug(editionSlug));
    }

    public byte[] getPublicParallelBook(String editionSlug, Language language) {
        Book primary = getBookBySlug(editionSlug);
        Book secondary = bookRepository.findAvailableLanguagesForBook(primary.getId()).stream()
                .filter(variant -> variant.getLanguage().equals(language))
                .map(variant -> getBookById(variant.getId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Book not found in this language"));
        if (primary.getId().equals(secondary.getId())) {
            throw new BadUserRequestActionException("This book is already in this language");
        }
        return publishedBookContentService.parallelTextFor(primary, secondary);
    }

    private Book getBookBySlug(String editionSlug) {
        return bookRepository
                .findByEditionSlug(editionSlug)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with slug: " + editionSlug));
    }

    public void addToFavorites(User user, UUID bookId, Language language) {
        Learner learner = learnerFinder.findLearner(user, language);
        Book book = getBookById(bookId);

        BookFavorite bookFavorite = new BookFavorite(learner, book);
        bookFavoriteRepository.save(bookFavorite);
    }

    public boolean deleteFromFavorites(User user, UUID bookId, Language language) {
        Learner learner = learnerFinder.findLearner(user, language);
        return bookFavoriteRepository.deleteByLearnerIdAndBookId(learner.getId(), bookId) > 0;
    }

    public Book getBookById(UUID bookId) {
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

    public boolean deleteBookProgress(User user, UUID bookId) {
        int deletedCount = learnerBookProgressRepository.deleteByUserIdAndBookId(user.getId(), bookId);
        return deletedCount > 0;
    }

    public void saveBookProgress(User user, UUID bookId, int progressPercentage) {
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
    public List<Language> getAvailableLanguagesForBook(UUID bookId) {
        return bookRepository.findAvailableLanguagesForBook(bookId).stream()
                .map(BookMiniProjection::getLanguage)
                .toList();
    }

    public BookMiniDetails getBookById(UUID userId, UUID bookId) {
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

    public BookDetails getBookById(User user, Language language, UUID bookId) {
        UUID learnerId = learnerFinder.findLearner(user, language).getId();
        Set<Language> fluentLanguages = userRepository.findFluentLangsById(user.getId());

        BookDetailsProjection projection = bookRepository
                .findBookDtoById(bookId, learnerId, fluentLanguages)
                .orElseThrow(EntityNotFoundException::new);

        List<BookLanguageVariant> availableLanguages =
                bookMapper.toMiniDto(bookRepository.findAvailableLanguagesForBook(bookId));
        Optional<BookFavorite> favorite = bookFavoriteRepository.findByLearnerIdAndBookId(learnerId, bookId);

        return bookMapper.toDetailsDto(projection, availableLanguages, favorite);
    }

    public byte[] getText(User user, UUID bookId) {
        Book book = getBookById(bookId);
        return publishedBookContentService.textFor(book);
    }

    public byte[] getParallelBook(User user, Language language, UUID bookId) {
        List<BookLanguageVariant> availableLanguages =
                bookMapper.toMiniDto(bookRepository.findAvailableLanguagesForBook(bookId));

        BookLanguageVariant miniDetails = availableLanguages.stream()
                .filter(book -> book.getLanguage().equals(language))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Book not found in this language"));

        UUID secondId = miniDetails.getId();

        if (bookId.equals(secondId)) {
            throw new BadUserRequestActionException("This book is already in this language");
        }

        return publishedBookContentService.parallelTextFor(getBookById(bookId), getBookById(secondId));
    }
}
