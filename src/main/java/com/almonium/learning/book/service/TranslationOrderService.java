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
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
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
    BookEmailService bookEmailService;
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

        List<User> users = orders.stream().map(TranslationOrder::getUser).toList();
        notificationService.notifyOfTranslationOrderCompletion(book.getTitle(), book.getLanguage(), users);
        bookEmailService.translationReady(users, book.getTitle(), book.getLanguage(), book.getEditionSlug());
    }

    /** Declining refunds the slot, drops the request out of the caller's list, and says so in a plain mail. */
    @Transactional
    public int declineTranslationOrders(UUID bookId, Language language) {
        List<TranslationOrder> orders = translationOrderRepository.findByBookIdAndLanguageAndStatus(
                bookId, language, TranslationOrderStatus.ASKED);
        if (orders.isEmpty()) {
            return 0;
        }
        Instant now = Instant.now();
        orders.forEach(order -> {
            order.setStatus(TranslationOrderStatus.DECLINED);
            order.setResolvedAt(now);
        });
        translationOrderRepository.saveAll(orders);
        bookEmailService.translationDeclined(
                orders.stream().map(TranslationOrder::getUser).toList(),
                orders.getFirst().getBook().getTitle(),
                language);
        return orders.size();
    }

    /** Withdrawing refunds the slot. A fulfilled request is history and cannot be taken back. */
    public boolean deleteTranslationOrder(UUID userId, UUID bookId, Language language) {
        return translationOrderRepository.deleteByUserIdAndBookIdAndLanguageAndStatus(
                        userId, bookId, language, TranslationOrderStatus.ASKED)
                > 0;
    }

    /** The fulfilment notice is shown until the reader opens it once; this is that once. */
    @Transactional
    public void markSeen(UUID userId, UUID orderId) {
        TranslationOrder order = translationOrderRepository
                .findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Translation request not found"));
        if (order.getSeenAt() == null) {
            order.setSeenAt(Instant.now());
            translationOrderRepository.save(order);
        }
    }

    @Transactional
    public TranslationOrderDto createTranslationOrder(User user, UUID bookId, Language language) {
        Optional<TranslationOrder> existing =
                translationOrderRepository.findByUserIdAndBookIdAndLanguage(user.getId(), bookId, language);
        if (existing.isPresent() && existing.get().getStatus() != TranslationOrderStatus.DECLINED) {
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

        // A declined request may be asked again: the row comes back as a fresh ask, spending this month's slot.
        TranslationOrder order = existing.orElseGet(() -> new TranslationOrder(user, book, language));
        order.setStatus(TranslationOrderStatus.ASKED);
        order.setResolvedAt(null);
        order.setSeenAt(null);
        order.setFulfilledBook(null);
        order.setCreatedAt(Instant.now());
        return bookMapper.toDto(translationOrderRepository.save(order));
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
