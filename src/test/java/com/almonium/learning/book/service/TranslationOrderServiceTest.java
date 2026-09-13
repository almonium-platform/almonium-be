package com.almonium.learning.book.service;

import static com.almonium.subscription.model.entity.enums.PlanFeature.MAX_TRANSLATION_REQUESTS_PER_MONTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.learning.book.dto.response.TranslationOrderDto;
import com.almonium.learning.book.dto.response.TranslationRequestQuotaDto;
import com.almonium.learning.book.mapper.BookMapper;
import com.almonium.learning.book.mapper.BookMapperImpl;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.TranslationOrder;
import com.almonium.learning.book.model.enums.TranslationOrderStatus;
import com.almonium.learning.book.repository.TranslationOrderRepository;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.service.BillingPeriodService;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TranslationOrderServiceTest {
    private static final List<TranslationOrderStatus> SPENDING =
            List.of(TranslationOrderStatus.ASKED, TranslationOrderStatus.READY);

    @Mock
    BookService bookService;

    @Mock
    NotificationService notificationService;

    @Mock
    BookEmailService bookEmailService;

    @Mock
    PlanValidationService planValidationService;

    @Mock
    PlanSubscriptionService subscriptionService;

    @Spy
    BillingPeriodService billingPeriodService = new BillingPeriodService();

    @Mock
    TranslationOrderRepository translationOrderRepository;

    @Spy
    BookMapper bookMapper = new BookMapperImpl();

    @InjectMocks
    TranslationOrderService service;

    @Test
    void asksForASecondLanguageOnABookAlreadyRequestedInAnother() {
        User user = user();
        Book book = originalBook();
        when(translationOrderRepository.findByUserIdAndBookIdAndLanguage(user.getId(), book.getId(), Language.UK))
                .thenReturn(Optional.empty());
        when(bookService.getBookById(book.getId())).thenReturn(book);
        when(bookService.getAvailableLanguagesForBook(book.getId())).thenReturn(List.of(Language.PL));
        stubQuota(user, 1L, 3);
        when(translationOrderRepository.save(any(TranslationOrder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TranslationOrderDto result = service.createTranslationOrder(user, book.getId(), Language.UK);

        verify(planValidationService).validatePlanFeature(user, MAX_TRANSLATION_REQUESTS_PER_MONTH, 2);
        assertThat(result.language()).isEqualTo(Language.UK);
        assertThat(result.status()).isEqualTo(TranslationOrderStatus.ASKED);
        assertThat(result.bookTitle()).isEqualTo("Effi Briest");
        assertThat(result.fulfilledBookId()).isNull();
    }

    @Test
    void refusesASecondRequestForTheSameBookAndLanguage() {
        User user = user();
        UUID bookId = UUID.randomUUID();
        TranslationOrder open = new TranslationOrder(user, originalBook(), Language.UK);
        when(translationOrderRepository.findByUserIdAndBookIdAndLanguage(user.getId(), bookId, Language.UK))
                .thenReturn(Optional.of(open));

        assertThatThrownBy(() -> service.createTranslationOrder(user, bookId, Language.UK))
                .isInstanceOf(ResourceConflictException.class);

        verify(planValidationService, never())
                .validatePlanFeature(
                        any(), eq(MAX_TRANSLATION_REQUESTS_PER_MONTH), org.mockito.ArgumentMatchers.anyInt());
    }

    @Test
    void aDeclinedRequestComesBackAsAFreshAskInsteadOfAConflict() {
        User user = user();
        Book book = originalBook();
        TranslationOrder declined = new TranslationOrder(user, book, Language.UK);
        declined.setStatus(TranslationOrderStatus.DECLINED);
        declined.setResolvedAt(Instant.now().minus(3, ChronoUnit.DAYS));
        declined.setCreatedAt(Instant.now().minus(40, ChronoUnit.DAYS));
        when(translationOrderRepository.findByUserIdAndBookIdAndLanguage(user.getId(), book.getId(), Language.UK))
                .thenReturn(Optional.of(declined));
        when(bookService.getBookById(book.getId())).thenReturn(book);
        when(bookService.getAvailableLanguagesForBook(book.getId())).thenReturn(List.of());
        stubQuota(user, 0L, 1);
        when(translationOrderRepository.save(any(TranslationOrder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TranslationOrderDto result = service.createTranslationOrder(user, book.getId(), Language.UK);

        assertThat(result.status()).isEqualTo(TranslationOrderStatus.ASKED);
        assertThat(declined.getResolvedAt()).isNull();
        assertThat(declined.getCreatedAt()).isAfter(Instant.now().minus(1, ChronoUnit.MINUTES));
        verify(planValidationService).validatePlanFeature(user, MAX_TRANSLATION_REQUESTS_PER_MONTH, 1);
    }

    @Test
    void markingSeenIsScopedToTheOwner() {
        UUID orderId = UUID.randomUUID();
        UUID stranger = UUID.randomUUID();
        when(translationOrderRepository.findByIdAndUserId(orderId, stranger)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.markSeen(stranger, orderId))
                .isInstanceOf(jakarta.persistence.EntityNotFoundException.class);

        User owner = user();
        TranslationOrder order = new TranslationOrder(owner, originalBook(), Language.UK);
        order.setStatus(TranslationOrderStatus.READY);
        when(translationOrderRepository.findByIdAndUserId(orderId, owner.getId()))
                .thenReturn(Optional.of(order));

        service.markSeen(owner.getId(), orderId);

        assertThat(order.getSeenAt()).isNotNull();
        verify(translationOrderRepository).save(order);
    }

    @Test
    void settlesOpenRequestsWhenTheTranslationIsPublished() {
        Book original = originalBook();
        Book translation = new Book();
        translation.setId(UUID.randomUUID());
        translation.setTitle("Effi Briest");
        translation.setEditionSlug("effi-briest-uk");
        translation.setLanguage(Language.UK);
        translation.setOriginalBook(original);
        User asker = user();
        TranslationOrder order = new TranslationOrder(asker, original, Language.UK);
        when(translationOrderRepository.findByBookIdAndLanguageAndStatus(
                        original.getId(), Language.UK, TranslationOrderStatus.ASKED))
                .thenReturn(List.of(order));

        service.publishTranslation(translation);

        assertThat(order.getStatus()).isEqualTo(TranslationOrderStatus.READY);
        assertThat(order.getFulfilledBook()).isEqualTo(translation);
        assertThat(order.getResolvedAt()).isNotNull();
        verify(notificationService).notifyOfTranslationOrderCompletion("Effi Briest", Language.UK, List.of(asker));
        verify(bookEmailService).translationReady(List.of(asker), "Effi Briest", Language.UK, "effi-briest-uk");
    }

    @Test
    void staysSilentWhenNobodyAskedForThePublishedTranslation() {
        Book original = originalBook();
        Book translation = new Book();
        translation.setId(UUID.randomUUID());
        translation.setLanguage(Language.UK);
        translation.setOriginalBook(original);
        when(translationOrderRepository.findByBookIdAndLanguageAndStatus(
                        original.getId(), Language.UK, TranslationOrderStatus.ASKED))
                .thenReturn(List.of());

        service.publishTranslation(translation);

        verify(notificationService, never()).notifyOfTranslationOrderCompletion(any(), any(), anyList());
        verify(translationOrderRepository, never()).saveAll(anyList());
    }

    @Test
    void decliningRefundsTheSlotByLeavingTheSpendingStatuses() {
        Book original = originalBook();
        TranslationOrder order = new TranslationOrder(user(), original, Language.UK);
        when(translationOrderRepository.findByBookIdAndLanguageAndStatus(
                        original.getId(), Language.UK, TranslationOrderStatus.ASKED))
                .thenReturn(List.of(order));

        int declined = service.declineTranslationOrders(original.getId(), Language.UK);

        assertThat(declined).isEqualTo(1);
        assertThat(order.getStatus()).isEqualTo(TranslationOrderStatus.DECLINED);
        assertThat(SPENDING).doesNotContain(order.getStatus());
        verify(notificationService, never()).notifyOfTranslationOrderCompletion(any(), any(), anyList());
        verify(bookEmailService).translationDeclined(List.of(order.getUser()), "Effi Briest", Language.UK);
    }

    @Test
    void withdrawingOnlyTouchesAnOpenRequest() {
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        when(translationOrderRepository.deleteByUserIdAndBookIdAndLanguageAndStatus(
                        userId, bookId, Language.UK, TranslationOrderStatus.ASKED))
                .thenReturn(0);

        assertThat(service.deleteTranslationOrder(userId, bookId, Language.UK)).isFalse();
    }

    @Test
    void countsAskedAndFulfilledRequestsAgainstTheSubscriberAnchoredMonth() {
        User user = user();
        stubQuota(user, 2L, 3);

        TranslationRequestQuotaDto quota = service.quota(user);

        assertThat(quota.limit()).isEqualTo(3);
        assertThat(quota.used()).isEqualTo(2);
        assertThat(quota.periodEndsAt()).isAfter(quota.periodStartsAt());
    }

    private void stubQuota(User user, long used, int limit) {
        when(subscriptionService.getActiveSub(user)).thenReturn(subscription());
        when(planValidationService.effectiveLimit(user, MAX_TRANSLATION_REQUESTS_PER_MONTH))
                .thenReturn(limit);
        when(translationOrderRepository.countByUserIdAndStatusInAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                        eq(user.getId()), eq(SPENDING), any(Instant.class), any(Instant.class)))
                .thenReturn(used);
    }

    private User user() {
        User user = new User();
        user.setId(UUID.randomUUID());
        return user;
    }

    private Book originalBook() {
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle("Effi Briest");
        book.setAuthor("Theodor Fontane");
        book.setLanguage(Language.DE);
        return book;
    }

    private PlanSubscription subscription() {
        PlanSubscription subscription = new PlanSubscription();
        subscription.setStartDate(Instant.now().minus(10, ChronoUnit.DAYS));
        subscription.setEndDate(Instant.now().plus(20, ChronoUnit.DAYS));
        return subscription;
    }
}
