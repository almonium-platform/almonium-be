package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.request.ApproveTranslationRequest;
import com.almonium.learning.book.dto.response.ProcessorTranslationEstimate;
import com.almonium.learning.book.dto.response.ProcessorTranslationStatus;
import com.almonium.learning.book.dto.response.TranslationJobDto;
import com.almonium.learning.book.dto.response.TranslationQueueDto;
import com.almonium.learning.book.dto.response.TranslationQueueRow;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.TranslationJob;
import com.almonium.learning.book.model.entity.TranslationOrder;
import com.almonium.learning.book.model.enums.TranslationJobPhase;
import com.almonium.learning.book.model.enums.TranslationOrderStatus;
import com.almonium.learning.book.repository.TranslationJobRepository;
import com.almonium.learning.book.repository.TranslationOrderRepository;
import com.almonium.subscription.model.entity.enums.Entitlement;
import com.almonium.subscription.service.EffectiveAccessService;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TranslationJobServiceTest {
    @Mock
    BookService bookService;

    @Mock
    BookProcessorClient processorClient;

    @Mock
    EffectiveAccessService effectiveAccessService;

    @Mock
    TranslationJobRepository jobRepository;

    @Mock
    TranslationOrderRepository orderRepository;

    @InjectMocks
    TranslationJobService service;

    @BeforeEach
    void budget() {
        ReflectionTestUtils.setField(service, "monthBudgetUsd", new BigDecimal("150"));
    }

    @Test
    void groupsAsksByPairAndSplitsThemByEntitlement() {
        Book effi = book("Effi Briest", "effi-briest-de-original");
        User premium = user();
        User free = user();
        User another = user();
        when(orderRepository.findByStatus(TranslationOrderStatus.ASKED))
                .thenReturn(List.of(
                        new TranslationOrder(premium, effi, Language.UK),
                        new TranslationOrder(free, effi, Language.UK),
                        new TranslationOrder(another, effi, Language.PL)));
        when(effectiveAccessService.entitlementsFor(anyCollection()))
                .thenReturn(Map.of(premium.getId(), Entitlement.PREMIUM, another.getId(), Entitlement.PREMIUM));
        when(jobRepository.findByPhaseIn(TranslationJobPhase.LIVE)).thenReturn(List.of());
        when(jobRepository.findByFinishedAtGreaterThanEqual(any(Instant.class))).thenReturn(List.of());
        when(jobRepository.findByApprovedAtGreaterThanEqualAndPhaseNot(
                        any(Instant.class), eq(TranslationJobPhase.CANCELLED)))
                .thenReturn(List.of());
        when(processorClient.estimateTranslation(eq("effi-briest-de-original"), any(), eq("quality"), eq("batch")))
                .thenReturn(new ProcessorTranslationEstimate(
                        UUID.randomUUID(), "effi-briest-de-original", 36, 1400, new BigDecimal("1.80")));

        TranslationQueueDto queue = service.queue();

        assertThat(queue.open()).isEqualTo(2);
        assertThat(queue.running()).isZero();
        assertThat(queue.rows()).hasSize(2);
        TranslationQueueRow first = queue.rows().getFirst();
        assertThat(first.language()).isEqualTo(Language.UK);
        assertThat(first.asks()).isEqualTo(2);
        assertThat(first.premiumAsks()).isEqualTo(1);
        assertThat(first.freeAsks()).isEqualTo(1);
        assertThat(first.estimatedCostUsd()).isEqualByComparingTo("1.80");
        assertThat(first.job()).isNull();
        assertThat(queue.monthSpendUsd()).isEqualByComparingTo("0");
        assertThat(queue.budgetExhausted()).isFalse();
    }

    @Test
    void approvingWritesTheEstimateAndMirrorsTheProcessorEdition() {
        Book effi = book("Effi Briest", "effi-briest-de-original");
        User operator = user();
        when(bookService.getBookById(effi.getId())).thenReturn(effi);
        when(bookService.getAvailableLanguagesForBook(effi.getId())).thenReturn(List.of(Language.DE));
        when(jobRepository.findByBookIdAndLanguageAndPhaseIn(effi.getId(), Language.UK, TranslationJobPhase.LIVE))
                .thenReturn(List.of());
        when(processorClient.estimateTranslation("effi-briest-de-original", Language.UK, "quality", "batch"))
                .thenReturn(new ProcessorTranslationEstimate(
                        UUID.randomUUID(), "effi-briest-de-original", 36, 1400, new BigDecimal("1.80")));
        when(jobRepository.findByApprovedAtGreaterThanEqualAndPhaseNot(
                        any(Instant.class), eq(TranslationJobPhase.CANCELLED)))
                .thenReturn(List.of(job(effi, Language.PL, TranslationJobPhase.PUBLISHED, "46.00")));
        when(jobRepository.save(any(TranslationJob.class))).thenAnswer(invocation -> {
            TranslationJob job = invocation.getArgument(0);
            if (job.getId() == null) job.setId(UUID.randomUUID());
            return job;
        });
        UUID editionId = UUID.randomUUID();
        when(processorClient.startTranslation(
                        any(UUID.class), eq("effi-briest-de-original"), eq(Language.UK), eq("quality"), eq("batch")))
                .thenReturn(new ProcessorTranslationStatus(
                        editionId,
                        "effi-briest-uk-parallel",
                        "processing",
                        "translating",
                        0,
                        36,
                        BigDecimal.ZERO,
                        "",
                        Instant.now(),
                        null,
                        null));

        TranslationJobDto job = service.approve(operator, effi.getId(), Language.UK, null);

        assertThat(job.phase()).isEqualTo(TranslationJobPhase.TRANSLATING);
        assertThat(job.estimatedCostUsd()).isEqualByComparingTo("1.80");
        assertThat(job.processorEditionSlug()).isEqualTo("effi-briest-uk-parallel");
        assertThat(job.progressTotal()).isEqualTo(36);
        assertThat(job.tier()).isEqualTo("quality");
    }

    @Test
    void approvingIsRefusedAtTheMonthsCeiling() {
        Book effi = book("Effi Briest", "effi-briest-de-original");
        when(bookService.getBookById(effi.getId())).thenReturn(effi);
        when(bookService.getAvailableLanguagesForBook(effi.getId())).thenReturn(List.of(Language.DE));
        when(jobRepository.findByBookIdAndLanguageAndPhaseIn(effi.getId(), Language.UK, TranslationJobPhase.LIVE))
                .thenReturn(List.of());
        when(processorClient.estimateTranslation("effi-briest-de-original", Language.UK, "draft", "inline"))
                .thenReturn(new ProcessorTranslationEstimate(
                        UUID.randomUUID(), "effi-briest-de-original", 36, 1400, new BigDecimal("2.40")));
        when(jobRepository.findByApprovedAtGreaterThanEqualAndPhaseNot(
                        any(Instant.class), eq(TranslationJobPhase.CANCELLED)))
                .thenReturn(List.of(job(effi, Language.PL, TranslationJobPhase.TRANSLATING, "149.00")));

        assertThatThrownBy(() -> service.approve(
                        user(), effi.getId(), Language.UK, new ApproveTranslationRequest("draft", "inline")))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("budget");
        verify(processorClient, never()).startTranslation(any(), any(), any(), any(), any());
        verify(jobRepository, never()).save(any());
    }

    @Test
    void aSecondApprovalOfARunningPairIsRefused() {
        Book effi = book("Effi Briest", "effi-briest-de-original");
        when(bookService.getBookById(effi.getId())).thenReturn(effi);
        when(bookService.getAvailableLanguagesForBook(effi.getId())).thenReturn(List.of(Language.DE));
        when(jobRepository.findByBookIdAndLanguageAndPhaseIn(effi.getId(), Language.UK, TranslationJobPhase.LIVE))
                .thenReturn(List.of(job(effi, Language.UK, TranslationJobPhase.ALIGNING, "1.80")));

        assertThatThrownBy(() -> service.approve(user(), effi.getId(), Language.UK, null))
                .isInstanceOf(ResourceConflictException.class);
    }

    @Test
    void publicationSettlesTheLiveJobWhateverPhaseWasLastSeen() {
        Book effi = book("Effi Briest", "effi-briest-de-original");
        Book translation = book("Effi Briest", "effi-briest-uk-parallel");
        translation.setLanguage(Language.UK);
        translation.setOriginalBook(effi);
        TranslationJob job = job(effi, Language.UK, TranslationJobPhase.QA_GATE, "1.80");
        job.setProcessorEditionId(UUID.randomUUID());
        when(jobRepository.findByBookIdAndLanguageAndPhaseIn(effi.getId(), Language.UK, TranslationJobPhase.LIVE))
                .thenReturn(List.of(job));
        when(processorClient.translationStatus(job.getProcessorEditionId()))
                .thenReturn(new ProcessorTranslationStatus(
                        job.getProcessorEditionId(),
                        "effi-briest-uk-parallel",
                        "published",
                        "published",
                        null,
                        null,
                        new BigDecimal("1.412"),
                        "",
                        null,
                        Instant.now(),
                        translation.getId()));

        service.settlePublished(translation);

        assertThat(job.getPhase()).isEqualTo(TranslationJobPhase.PUBLISHED);
        assertThat(job.getPublishedBook()).isEqualTo(translation);
        assertThat(job.getActualCostUsd()).isEqualByComparingTo("1.412");
        assertThat(job.getFinishedAt()).isNotNull();
        verify(jobRepository).saveAll(List.of(job));
    }

    @Test
    void theBudgetMonthStartsAtUtcMidnightOnTheFirst() {
        Instant start = TranslationJobService.monthStart(Instant.parse("2026-09-13T22:15:00Z"));

        assertThat(start).isEqualTo(Instant.parse("2026-09-01T00:00:00Z"));
    }

    private static Book book(String title, String slug) {
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle(title);
        book.setAuthor("Theodor Fontane");
        book.setEditionSlug(slug);
        book.setLanguage(Language.DE);
        return book;
    }

    private static TranslationJob job(Book book, Language language, TranslationJobPhase phase, String estimate) {
        TranslationJob job = new TranslationJob();
        job.setId(UUID.randomUUID());
        job.setBook(book);
        job.setLanguage(language);
        job.setPhase(phase);
        job.setEstimatedCostUsd(new BigDecimal(estimate));
        job.setApprovedAt(Instant.now());
        job.setFinishedAt(phase.isLive() ? null : Instant.now());
        job.setTier("quality");
        job.setMode("batch");
        job.setError("");
        return job;
    }

    private static User user() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(user.getId() + "@example.com");
        return user;
    }
}
