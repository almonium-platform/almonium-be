package com.almonium.learning.wanted;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.PostgresContainer;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.book.service.BookEmailService;
import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.catalog.repository.CatalogStore;
import com.almonium.learning.catalog.repository.CatalogStore.WorkRow;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.wanted.dto.request.WantRequest;
import com.almonium.learning.wanted.dto.response.WantedRow;
import com.almonium.learning.wanted.model.enums.WantStatus;
import com.almonium.learning.wanted.service.DemandService;
import com.almonium.learning.wanted.service.PreparationService;
import com.almonium.learning.wanted.service.WantService;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import tools.jackson.databind.ObjectMapper;

/** Wanting, ranking, withdrawing, editorial's decisions and an arrival, against PostgreSQL. */
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@Import({WantService.class, DemandService.class, CatalogStore.class, WantedFlowTest.Beans.class})
@Sql(scripts = "classpath:db/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class WantedFlowTest {
    @TestConfiguration
    @EnableJpaAuditing
    static class Beans {
        @Bean
        Clock clock() {
            return Clock.fixed(Instant.parse("2026-10-07T00:00:00Z"), ZoneOffset.UTC);
        }

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @MockitoBean
    NotificationService notificationService;

    @MockitoBean
    BookEmailService bookEmailService;

    @MockitoBean
    PreparationService preparationService;

    @Autowired
    WantService wants;

    @Autowired
    DemandService demand;

    @Autowired
    CatalogStore catalogStore;

    @Autowired
    UserRepository users;

    @Autowired
    BookRepository books;

    @Autowired
    EntityManager entityManager;

    User john;
    User jane;
    UUID hound;
    UUID study;
    UUID lolita;

    @BeforeEach
    void setUp() {
        john = users.findByEmail("john@email.com").orElseThrow();
        jane = users.findByEmail("jane@email.com").orElseThrow();
        hound = work("The Hound of the Baskervilles", RightsStatus.FREE);
        study = work("A Study in Scarlet", RightsStatus.FREE);
        lolita = work("Lolita", RightsStatus.PROTECTED);
    }

    UUID work(String title, RightsStatus status) {
        return work(title, status, null);
    }

    UUID work(String title, RightsStatus status, Integer freeFrom) {
        UUID id = UUID.randomUUID();
        catalogStore.upsertWorks(
                List.of(new WorkRow(
                        id,
                        "Q" + (System.nanoTime() % 1_000_000_000L),
                        null,
                        title,
                        null,
                        "en",
                        1900,
                        "Someone",
                        List.of(new CatalogAuthor("Someone", null, 1850, 1930, false, null)),
                        10,
                        status,
                        freeFrom)),
                Instant.now());
        return id;
    }

    @Test
    void theQueueRanksByReadersAndTheToastSaysWhereTheWantLanded() {
        assertThat(wants.want(john, new WantRequest(study, Language.EN)).rank()).isEqualTo(1);
        wants.want(john, new WantRequest(hound, Language.EN));
        var receipt = wants.want(jane, new WantRequest(hound, Language.EN));

        assertThat(receipt.rank()).isEqualTo(1);
        assertThat(receipt.readers()).isNull();
        var queue = wants.queue(jane, false);
        assertThat(queue.queue()).extracting(WantedRow::catalogWorkId).containsExactly(hound, study);
        assertThat(queue.queue()).extracting(WantedRow::wantedByMe).containsExactly(true, false);
        assertThat(wants.queue(jane, true).queue())
                .extracting(WantedRow::catalogWorkId)
                .containsExactly(hound);
        assertThat(queue.mineCount()).isEqualTo(1);
    }

    @Test
    void wantingTwiceIsOneWantAndTappingAgainWithdrawsIt() {
        wants.want(john, new WantRequest(hound, Language.DE));
        wants.want(john, new WantRequest(hound, Language.DE));
        assertThat(wants.queue(john, true).queue()).hasSize(1);

        wants.withdraw(john, hound, Language.DE);

        assertThat(wants.queue(john, false).queue()).isEmpty();
    }

    @Test
    void aWorkUnderCopyrightCannotBeWanted() {
        assertThatThrownBy(() -> wants.want(john, new WantRequest(lolita, Language.EN)))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("under copyright");
    }

    @Test
    void aWorkThatFreesUpWithinFiveYearsCanBeWantedAheadOfTheDay() {
        UUID pooh = work("Winnie-the-Pooh", RightsStatus.PROTECTED, 2027);
        UUID farOff = work("Lolita, again", RightsStatus.PROTECTED, 2048);

        assertThat(wants.want(john, new WantRequest(pooh, Language.EN)).rank()).isEqualTo(1);
        assertThatThrownBy(() -> wants.want(john, new WantRequest(farOff, Language.EN)))
                .isInstanceOf(ResourceConflictException.class);
    }

    @Test
    void editorialStartsAWorkAndItsArrivalSettlesEveryWantAndTellsEachReaderOnce() {
        wants.want(john, new WantRequest(hound, Language.EN));
        wants.want(jane, new WantRequest(hound, Language.EN));
        wants.want(jane, new WantRequest(study, Language.EN));

        demand.start(null, hound, Language.EN, null);
        var queue = wants.queue(john, false);
        assertThat(queue.beingPrepared()).extracting(WantedRow::catalogWorkId).containsExactly(hound);
        assertThat(queue.beingPrepared().getFirst().status()).isEqualTo(WantStatus.IN_PROGRESS);
        assertThat(queue.queue()).extracting(WantedRow::catalogWorkId).containsExactly(study);
        assertThat(demand.demand(30).wanted())
                .extracting(row -> row.status())
                .containsExactly(WantStatus.IN_PROGRESS, WantStatus.OPEN);

        catalogStore.linkWorkSlug(hound, "hound");
        Book book = new Book();
        book.setEditionSlug("hound-en");
        book.setWorkSlug("hound");
        book.setSourceHash("a".repeat(64));
        book.setTitle("The Hound of the Baskervilles");
        book.setAuthor("Arthur Conan Doyle");
        book.setPublicationYear(1902);
        book.setLanguage(Language.EN);
        book.setOriginalLanguage(Language.EN);
        book.setCefrLevel(CEFR.B2);
        book.setEditionType("original");
        wants.settlePublished(books.saveAndFlush(book));
        entityManager.flush();
        entityManager.clear();

        var after = wants.queue(john, false);
        assertThat(after.beingPrepared()).isEmpty();
        assertThat(after.recentlyArrived()).singleElement().satisfies(row -> {
            assertThat(row.editionSlug()).isEqualTo("hound-en");
            assertThat(row.cefrLevel()).isEqualTo(CEFR.B2);
        });
        verify(notificationService, times(2))
                .notifyOfRequestPublished(
                        any(), any(), eq("The Hound of the Baskervilles"), any(), any(), eq("/books/hound-en"));
        verify(bookEmailService, times(2)).requestPublished(any(), eq("The Hound of the Baskervilles"), any(), any());
        assertThatThrownBy(() -> wants.want(john, new WantRequest(hound, Language.EN)))
                .hasMessageContaining("already on the shelf");
    }

    @Test
    void aRowWhosePreparationFailedCanBeStartedAgainWithASource() {
        wants.want(john, new WantRequest(study, Language.EN));
        UUID source = UUID.randomUUID();
        demand.start(null, study, Language.EN, source);
        demand.start(null, study, Language.EN, source);

        verify(preparationService, times(2)).start(null, study, Language.EN, source);
        assertThat(demand.demand(30).wanted()).extracting(row -> row.status()).containsExactly(WantStatus.IN_PROGRESS);
        assertThatThrownBy(() -> demand.start(null, study, Language.EN, null))
                .hasMessageContaining("Nobody is waiting");
    }

    @Test
    void aDeclinedWorkLeavesTheQueue() {
        wants.want(john, new WantRequest(study, Language.EN));

        demand.decline(null, study, Language.EN);

        assertThat(wants.queue(john, false).queue()).isEmpty();
        assertThat(demand.demand(30).wanted()).isEmpty();
    }

    @Test
    void searchesFoldTypingIntoOneAndCountCopyrightAndEmptyResults() {
        Instant now = Instant.parse("2026-10-06T12:00:00Z");
        var lolitaHit = new CatalogStore.Hit(
                lolita, "Lolita", null, "en", 1955, "Vladimir Nabokov", 60, null, RightsStatus.PROTECTED, 2048, 2.0);
        catalogStore.logSearch("lol", lolitaHit, null, "g:a", now);
        catalogStore.logSearch("lolita", lolitaHit, null, "g:a", now.plusSeconds(2));
        catalogStore.logSearch("lolita", lolitaHit, john.getId(), "m:" + john.getId(), now);
        catalogStore.logSearch("zzqx", null, null, "g:a", now.plusSeconds(300));

        var report = demand.demand(30);

        assertThat(report.underCopyright()).singleElement().satisfies(row -> {
            assertThat(row.catalogWorkId()).isEqualTo(lolita);
            assertThat(row.searches()).isEqualTo(2);
            assertThat(row.readers()).isEqualTo(2);
        });
        assertThat(report.noMatch())
                .singleElement()
                .satisfies(row -> assertThat(row.query()).isEqualTo("zzqx"));

        demand.alias(null, "ZZQX", study);
        assertThat(catalogStore.search("zzqx", 5)).extracting(hit -> hit.id()).containsExactly(study);
    }
}
