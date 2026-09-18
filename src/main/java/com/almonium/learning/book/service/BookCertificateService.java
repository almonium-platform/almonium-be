package com.almonium.learning.book.service;

import static lombok.AccessLevel.PRIVATE;

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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Issues and serves the book-completion certificate (design K). Reaching the end of the last chapter is the
 * completion signal, so issuing also lands the reader's progress at 100%; the record is then frozen except for the
 * saved-word count, which the owner sees live, and the one switch that decides whether the public page answers.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional
public class BookCertificateService {
    static final int FINISHED_PERCENTAGE = 100;

    BookCertificateRepository certificateRepository;
    BookService bookService;
    PublishedBookContentService publishedBookContentService;
    LearnerFinder learnerFinder;
    LearningItemRepository learningItemRepository;
    RarestWordsSelector rarestWordsSelector;

    /** The certificate for a book the reader has just finished; the same one again on a second visit to the end. */
    public BookCertificateDto issue(User user, UUID bookId) {
        Book book = bookService.getBookById(bookId);
        BookCertificate certificate = certificateRepository
                .findByUserIdAndBookId(user.getId(), bookId)
                .orElseGet(() -> {
                    bookService.saveBookProgress(user, bookId, FINISHED_PERCENTAGE, book.getChapterCount());
                    // On by default: the username is already public on the profile page, so nothing new is exposed.
                    BookCertificate issued = new BookCertificate(user, book, Instant.now(), true, rarestWords(book));
                    log.info("Issued a certificate for user {} and book {}", user.getId(), bookId);
                    return issued;
                });
        return toDto(refreshSaved(user, certificate));
    }

    /** The owner's certificate, if one has been issued; the shelf's Certificate action reads this. */
    @Transactional(readOnly = true)
    public BookCertificateDto get(User user, UUID bookId) {
        return toDto(refreshSaved(user, ownRecord(user, bookId)));
    }

    public BookCertificateDto setPublicPage(User user, UUID bookId, boolean publicPage) {
        BookCertificate certificate = ownRecord(user, bookId);
        certificate.setPublicPage(publicPage);
        return toDto(certificateRepository.save(certificate));
    }

    /** What a stranger sees at {@code /read/@username/editionSlug}; a page turned off is not found, not forbidden. */
    @Transactional(readOnly = true)
    public BookCertificateDto publicView(String username, String editionSlug) {
        return toDto(certificateRepository
                .findPublic(username.toLowerCase(Locale.ROOT), editionSlug)
                .orElseThrow(() -> new EntityNotFoundException("No public certificate at that address")));
    }

    private BookCertificate ownRecord(User user, UUID bookId) {
        return certificateRepository
                .findByUserIdAndBookId(user.getId(), bookId)
                .orElseThrow(() -> new EntityNotFoundException("No certificate for this book yet"));
    }

    /** The saved count is the one live number: the owner keeps saving words after the last page. */
    private BookCertificate refreshSaved(User user, BookCertificate certificate) {
        Learner learner = learnerFinder.findLearner(user, certificate.getBook().getLanguage());
        certificate.setWordsSaved((int) learningItemRepository.countByOwnerAndSourceBookId(
                learner, certificate.getBook().getId()));
        return certificateRepository.save(certificate);
    }

    /** Every chapter's curated words, gathered once; a chapter whose words are not ready simply contributes none. */
    private List<String> rarestWords(Book book) {
        List<ChapterVocabulary> vocabularies = new ArrayList<>();
        try {
            for (BookChapter chapter : publishedBookContentService.chaptersFor(book)) {
                try {
                    vocabularies.add(publishedBookContentService.vocabularyFor(book, chapter.sequence()));
                } catch (RuntimeException unavailable) {
                    log.warn(
                            "No vocabulary for chapter {} of {}: {}",
                            chapter.sequence(),
                            book.getEditionSlug(),
                            unavailable.getMessage());
                }
            }
        } catch (RuntimeException unavailable) {
            log.warn("No chapters for {}: {}", book.getEditionSlug(), unavailable.getMessage());
        }
        return rarestWordsSelector.rarest(vocabularies);
    }

    private static BookCertificateDto toDto(BookCertificate certificate) {
        Book book = certificate.getBook();
        return new BookCertificateDto(
                certificate.getUser().getUsername(),
                book.getEditionSlug(),
                book.getTitle(),
                book.getAuthor(),
                book.getLanguage(),
                certificate.getWords(),
                certificate.getWordsRead(),
                certificate.getWordsSaved(),
                certificate.getFinishedAt(),
                certificate.isPublicPage());
    }
}
