package com.almonium.learning.book.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.response.TranslationOrderDto;
import com.almonium.learning.book.mapper.BookMapper;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.TranslationOrder;
import com.almonium.learning.book.repository.TranslationOrderRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TranslationOrderService {
    TranslationOrderRepository translationOrderRepository;
    BookService bookService;
    BookMapper bookMapper;

    @Transactional
    public TranslationOrderDto createTranslationOrder(User user, Long bookId, Language language) {
        if (translationOrderRepository.existsByUserIdAndBookId(user.getId(), bookId)) {
            throw new ResourceConflictException("You already have a translation order for this book");
        }

        Book book = bookService.getBookById(bookId);
        if (book.getLanguage().equals(language)) {
            throw new BadUserRequestActionException("%s is original language".formatted(language));
        }

        bookService.getAvailableLanguagesForBook(book.getId()).stream()
                .filter(availableLanguage -> availableLanguage.equals(language))
                .findFirst()
                .orElseThrow(() ->
                        new BadUserRequestActionException("Book is already translated to %s".formatted(language)));

        return bookMapper.toDto(translationOrderRepository.save(new TranslationOrder(user, book, language)));
    }
}
