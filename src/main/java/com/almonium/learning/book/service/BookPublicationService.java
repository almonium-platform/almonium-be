package com.almonium.learning.book.service;

import com.almonium.learning.book.dto.request.BookPublicationRequest;
import com.almonium.learning.book.dto.request.BookWithdrawalRequest;
import com.almonium.learning.book.dto.response.BookPublicationResponse;
import com.almonium.learning.book.dto.response.BookWithdrawalResponse;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookPublicationService {
    private final BookRepository bookRepository;
    private final TranslationOrderService translationOrderService;
    private final TranslationJobService translationJobService;
    private final LibrarySuggestionService librarySuggestionService;
    private final BookRequestService bookRequestService;

    public BookPublicationResponse publish(BookPublicationRequest request) {
        Book book = bookRepository.findAnyByEditionSlug(request.editionSlug()).orElseGet(Book::new);
        // Publishing again is how a withdrawn book comes back.
        book.setWithdrawnAt(null);
        book.setEditionSlug(request.editionSlug());
        book.setWorkSlug(request.workSlug());
        book.setSourceHash(request.sourceHash());
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setDescription(request.description());
        book.setLanguage(request.language());
        book.setWordCount(request.wordCount());
        book.setChapterCount(request.chapterCount());
        book.setPublicationYear(request.publicationYear());
        book.setCoverUrl(request.coverUrl());
        book.setCefrLevel(request.cefrLevel());
        book.setOriginalLanguage(request.originalLanguage());
        book.setEditionType(request.editionType());
        book.setTranslator(request.translator());
        book.setEditionNote(request.editionNote());
        if (request.sourceEditionSlug() != null) {
            book.setOriginalBook(bookRepository
                    .findByEditionSlug(request.sourceEditionSlug())
                    .orElseThrow(() -> new EntityNotFoundException("Source edition must be published first")));
        } else {
            book.setOriginalBook(null);
        }
        Book saved = bookRepository.save(book);
        if (saved.getOriginalBook() != null
                && ("machine_translation".equals(saved.getEditionType())
                        || "human_translation".equals(saved.getEditionType()))) {
            translationOrderService.publishTranslation(saved);
            translationJobService.settlePublished(saved);
        }
        if (request.externalJobId() != null) {
            librarySuggestionService.settlePublished(request.externalJobId(), saved);
        }
        bookRequestService.settlePublished(saved);
        return new BookPublicationResponse(saved.getId());
    }

    public BookWithdrawalResponse withdraw(BookWithdrawalRequest request) {
        Optional<Book> found = bookRepository.findAnyByEditionSlug(request.editionSlug());
        if (found.isEmpty()) {
            // An edition we never published is already as withdrawn as it gets.
            // Saying so lets a retried takedown finish instead of stalling.
            return new BookWithdrawalResponse(false, null);
        }
        Book book = found.get();
        long translations = bookRepository.countTranslationsOf(book.getId());
        if (translations > 0) {
            throw new IllegalStateException(
                    "Withdraw the %d translation(s) generated from this book first".formatted(translations));
        }
        if (book.getWithdrawnAt() != null) {
            return new BookWithdrawalResponse(false, book.getId());
        }
        book.setWithdrawnAt(Instant.now());
        bookRepository.save(book);
        return new BookWithdrawalResponse(true, book.getId());
    }
}
