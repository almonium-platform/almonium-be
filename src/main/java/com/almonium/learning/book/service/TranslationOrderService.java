package com.almonium.learning.book.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.learning.book.dto.response.TranslationOrderDto;
import com.almonium.learning.book.mapper.BookMapper;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.TranslationOrder;
import com.almonium.learning.book.repository.TranslationOrderRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TranslationOrderService {
    BookService bookService;
    NotificationService notificationService;

    TranslationOrderRepository translationOrderRepository;

    BookMapper bookMapper;

    @SuppressWarnings("unused")
    public void publishTranslation(Book book) {
        List<TranslationOrder> translationOrders =
                translationOrderRepository.findByBookIdAndLanguage(book.getId(), book.getLanguage());

        List<UUID> ordersIds = new ArrayList<>();
        List<User> users = new ArrayList<>();

        translationOrders.forEach(translationOrder -> {
            ordersIds.add(translationOrder.getId());
            users.add(translationOrder.getUser());
        });

        notificationService.notifyOfTranslationOrderCompletion(book.getTitle(), book.getLanguage(), users);

        translationOrderRepository.deleteAllByIdInBatch(ordersIds);
    }

    public boolean deleteTranslationOrder(UUID userId, Long bookId, Language language) {
        return translationOrderRepository.deleteByUserIdAndBookIdAndLanguage(userId, bookId, language) > 0;
    }

    @Transactional
    public TranslationOrderDto createTranslationOrder(User user, Long bookId, Language language) {
        if (translationOrderRepository.existsByUserIdAndBookId(user.getId(), bookId)) {
            throw new ResourceConflictException("You already have a translation order for this book");
        }

        Book book = bookService.getBookById(bookId);
        if (book.getLanguage().equals(language)) {
            throw new BadUserRequestActionException("%s is original language".formatted(language));
        }

        if (book.getOriginalBook() != null) {
            throw new BadUserRequestActionException("You cannot order a translation for a translation");
        }

        bookService.getAvailableLanguagesForBook(book.getId()).stream()
                .filter(availableLanguage -> availableLanguage.equals(language))
                .findAny()
                .ifPresent(existing -> {
                    throw new BadUserRequestActionException(
                            "%s is already available for this book".formatted(language));
                });

        return bookMapper.toDto(translationOrderRepository.save(new TranslationOrder(user, book, language)));
    }
}
