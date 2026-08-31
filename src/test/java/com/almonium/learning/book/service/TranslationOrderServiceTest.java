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
        when(translationOrderRepository.existsByUserIdAndBookIdAndLanguage(user.getId(), book.getId(), Language.UK))
                .thenReturn(false);
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
        when(translationOrderRepository.existsByUserIdAndBookIdAndLanguage(user.getId(), bookId, Language.UK))
                .thenReturn(true);

        assertThatThrownBy(() -> service.createTranslationOrder(user, bookId, Language.UK))
                .isInstanceOf(ResourceConflictException.class);

        verify(planValidationService, never())
                .validatePlanFeature(
                        any(), eq(MAX_TRANSLATION_REQUESTS_PER_MONTH), org.mockito.ArgumentMatchers.anyInt());
    }

    @Test
    void settlesOpenRequestsWhenTheTranslationIsPublished() {
        Book original = originalBook();
        Book translation = new Book();
        translation.setId(UUID.randomUUID());
        translation.setTitle("Effi Briest");
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
