package com.almonium.learning.book.service;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.learning.book.dto.request.BookPublicationRequest;
import com.almonium.learning.book.dto.response.BookPublicationResponse;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookPublicationService {
    private final BookRepository bookRepository;

    public BookPublicationResponse publish(BookPublicationRequest request) {
        Book book =
                bookRepository.findByProcessorEditionSlug(request.editionSlug()).orElseGet(Book::new);
        book.setProcessorEditionSlug(request.editionSlug());
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setLanguage(request.language());
        book.setWordCount(request.wordCount());
        book.setPublicationYear(request.firstPublishedYear() == null ? 0 : request.firstPublishedYear());
        book.setCoverImageUrl("");
        book.setRating(0);
        book.setLevelFrom(CEFR.C2);
        book.setLevelTo(CEFR.C2);
        book.setDescription("Published from Almonium Books.");
        book.setTranslator(request.translator());
        if (request.sourceEditionSlug() != null) {
            book.setOriginalBook(bookRepository
                    .findByProcessorEditionSlug(request.sourceEditionSlug())
                    .orElseThrow(() -> new EntityNotFoundException("Source edition must be published first")));
        } else {
            book.setOriginalBook(null);
        }
        return new BookPublicationResponse(bookRepository.save(book).getId());
    }
}
