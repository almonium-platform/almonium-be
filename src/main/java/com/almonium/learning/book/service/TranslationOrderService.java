package com.almonium.learning.book.service;

import static com.almonium.subscription.model.entity.enums.PlanFeature.MAX_TRANSLATION_REQUESTS_PER_MONTH;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.learning.book.dto.response.TranslationOrderDto;
import com.almonium.learning.book.dto.response.TranslationRequestQuotaDto;
import com.almonium.learning.book.mapper.BookMapper;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.TranslationOrder;
import com.almonium.learning.book.model.enums.TranslationOrderStatus;
import com.almonium.learning.book.repository.TranslationOrderRepository;
import com.almonium.subscription.service.BillingPeriodService;
import com.almonium.subscription.service.BillingPeriodService.BillingPeriod;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
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
    /** Asked requests have spent a slot; so have fulfilled ones. Declines and withdrawals refund it. */
    private static final List<TranslationOrderStatus> SPENDING_STATUSES =
            List.of(TranslationOrderStatus.ASKED, TranslationOrderStatus.READY);

    BookService bookService;
    NotificationService notificationService;
    PlanValidationService planValidationService;
    PlanSubscriptionService subscriptionService;
    BillingPeriodService billingPeriodService;

    TranslationOrderRepository translationOrderRepository;

    BookMapper bookMapper;

    /**
     * Settles every open request for this pair. Fulfilment is announced when the translation is
     * published, never when it is approved: approving is a promise, publishing is the thing itself.
     */
    @Transactional
    public void publishTranslation(Book book) {
        UUID sourceBookId = book.getOriginalBook() == null
                ? book.getId()
                : book.getOriginalBook().getId();
        List<TranslationOrder> orders = translationOrderRepository.findByBookIdAndLanguageAndStatus(
                sourceBookId, book.getLanguage(), TranslationOrderStatus.ASKED);
        if (orders.isEmpty()) {
            return;
        }

        Instant now = Instant.now();
        orders.forEach(order -> {
            order.setStatus(TranslationOrderStatus.READY);
            order.setFulfilledBook(book);
            order.setResolvedAt(now);
        });
        translationOrderRepository.saveAll(orders);

        notificationService.notifyOfTranslationOrderCompletion(
                book.getTitle(),
                book.getLanguage(),
                orders.stream().map(TranslationOrder::getUser).toList());
    }

    /** Declining refunds the slot and drops the request out of the caller's list. */
    @Transactional
    public int declineTranslationOrders(UUID bookId, Language language) {
        List<TranslationOrder> orders = translationOrderRepository.findByBookIdAndLanguageAndStatus(
                bookId, language, TranslationOrderStatus.ASKED);
        Instant now = Instant.now();
        orders.forEach(order -> {
            order.setStatus(TranslationOrderStatus.DECLINED);
            order.setResolvedAt(now);
        });
        translationOrderRepository.saveAll(orders);
        return orders.size();
    }

    /** Withdrawing refunds the slot. A fulfilled request is history and cannot be taken back. */
    public boolean deleteTranslationOrder(UUID userId, UUID bookId, Language language) {
        return translationOrderRepository.deleteByUserIdAndBookIdAndLanguageAndStatus(
                        userId, bookId, language, TranslationOrderStatus.ASKED)
                > 0;
    }

    @Transactional
    public TranslationOrderDto createTranslationOrder(User user, UUID bookId, Language language) {
        if (translationOrderRepository.existsByUserIdAndBookIdAndLanguage(user.getId(), bookId, language)) {
            throw new ResourceConflictException("You already asked for this book in this language");
        }

        Book book = bookService.getBookById(bookId);
        if (book.getLanguage().equals(language)) {
            throw new BadUserRequestActionException("%s is original language".formatted(language));
        }

        if (book.getOriginalBook() != null) {
            throw new BadUserRequestActionException("You cannot order a translation for a translation");
        }

        if (bookService.getAvailableLanguagesForBook(book.getId()).contains(language)) {
            throw new BadUserRequestActionException("%s is already available for this book".formatted(language));
        }

        TranslationRequestQuotaDto quota = quota(user);
        planValidationService.validatePlanFeature(user, MAX_TRANSLATION_REQUESTS_PER_MONTH, quota.used() + 1);

        return bookMapper.toDto(translationOrderRepository.save(new TranslationOrder(user, book, language)));
    }

    @Transactional(readOnly = true)
    public List<TranslationOrderDto> list(User user) {
        return bookMapper.toOrderDtos(translationOrderRepository.findByUserIdAndStatusInOrderByCreatedAtDesc(
                user.getId(), SPENDING_STATUSES));
    }

    @Transactional(readOnly = true)
    public TranslationRequestQuotaDto quota(User user) {
        BillingPeriod period =
                billingPeriodService.currentPeriod(subscriptionService.getActiveSub(user), Instant.now());
        int limit = planValidationService.effectiveLimit(user, MAX_TRANSLATION_REQUESTS_PER_MONTH);
        long used = translationOrderRepository.countByUserIdAndStatusInAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                user.getId(), SPENDING_STATUSES, period.startsAt(), period.endsAt());
        return new TranslationRequestQuotaDto(limit, Math.toIntExact(used), period.startsAt(), period.endsAt());
    }
}
