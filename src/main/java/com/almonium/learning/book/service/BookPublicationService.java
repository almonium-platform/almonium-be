package com.almonium.learning.book.service;

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
        Book book = bookRepository.findByEditionSlug(request.editionSlug()).orElseGet(Book::new);
        book.setEditionSlug(request.editionSlug());
        book.setWorkSlug(request.workSlug());
        book.setSourceHash(request.sourceHash());
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setLanguage(request.language());
        book.setWordCount(request.wordCount());
        book.setPublicationYear(request.publicationYear());
        book.setCoverUrl(request.coverUrl());
        book.setCefrLevel(request.cefrLevel());
        book.setOriginalLanguage(request.originalLanguage());
        book.setEditionType(request.editionType());
        book.setTranslator(request.translator());
        if (request.sourceEditionSlug() != null) {
            book.setOriginalBook(bookRepository
                    .findByEditionSlug(request.sourceEditionSlug())
                    .orElseThrow(() -> new EntityNotFoundException("Source edition must be published first")));
        } else {
            book.setOriginalBook(null);
        }
        return new BookPublicationResponse(bookRepository.save(book).getId());
    }
}
