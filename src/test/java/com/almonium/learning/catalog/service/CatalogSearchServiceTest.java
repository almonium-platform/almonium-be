package com.almonium.learning.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.service.CurrentMember;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.dto.response.CatalogHit;
import com.almonium.learning.catalog.dto.response.CatalogHit.CatalogState;
import com.almonium.learning.catalog.dto.response.WorkUnlocksView;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogStore;
import com.almonium.learning.catalog.repository.CatalogStore.Hit;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.rights.engine.CopyrightEngine;
import com.almonium.learning.rights.engine.CountryTerms;
import com.almonium.learning.rights.model.AuthorFacts;
import com.almonium.learning.rights.model.RightsFacts;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.service.ProductionRights;
import com.almonium.learning.rights.service.ReaderLocation;
import com.almonium.learning.rights.service.ReadingRights;
import com.almonium.learning.rights.service.UnlockDates;
import com.almonium.learning.wanted.repository.WantRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogSearchServiceTest {
    static final Clock CLOCK = Clock.fixed(Instant.parse("2026-10-07T00:00:00Z"), ZoneOffset.UTC);

    CatalogStore store = mock(CatalogStore.class);
    CatalogWorkRepository works = mock(CatalogWorkRepository.class);
    BookRepository books = mock(BookRepository.class);
    ReadingRights readingRights = mock(ReadingRights.class);
    ReaderLocation readerLocation = mock(ReaderLocation.class);
    UnlockDates unlockDates = new UnlockDates(
            new CopyrightEngine(), new ProductionRights(new CopyrightEngine(), CLOCK, List.of("UA", "DE")));
    WantRepository wants = mock(WantRepository.class);
    CurrentMember currentMember = mock(CurrentMember.class);
    SearchRequester requester = new SearchRequester();
    CatalogSearchService service = new CatalogSearchService(
            store, works, books, readingRights, readerLocation, unlockDates, wants, currentMember, requester, CLOCK);

    @BeforeEach
    void readerInGermany() {
        when(readerLocation.country()).thenReturn("DE");
        when(readerLocation.detectedCountry()).thenReturn(Optional.of("DE"));
    }

    @Test
    void aProtectedHitSaysWhenTheReaderCanOpenItAndWhereItIsFreeAlready() {
        Hit pooh = new Hit(
                UUID.randomUUID(),
                "Winnie-the-Pooh",
                null,
                "en",
                1926,
                "A. A. Milne",
                60,
                null,
                RightsStatus.PROTECTED,
                2027,
                1.0);
        CatalogWork work = mock(CatalogWork.class);
        when(work.getId()).thenReturn(pooh.id());
        when(work.rightsFacts()).thenReturn(new RightsFacts(1926, List.of(AuthorFacts.of("A. A. Milne", 1882, 1956))));
        when(works.findAllById(List.of(pooh.id()))).thenReturn(List.of(work));
        when(store.search("pooh", 20)).thenReturn(List.of(pooh));

        CatalogHit hit = service.search("Pooh", 20).getFirst();

        assertThat(hit.state()).isEqualTo(CatalogState.UNDER_COPYRIGHT);
        assertThat(hit.readerCountry()).isEqualTo("DE");
        assertThat(hit.readerFreeFrom()).isEqualTo(2027);
        assertThat(hit.freeIn()).containsExactly("US", "CA", "IN");
    }

    @Test
    void theUnlockMapListsEveryCountryForOneWork() {
        UUID id = UUID.randomUUID();
        CatalogWork work = mock(CatalogWork.class);
        when(work.getId()).thenReturn(id);
        when(work.getTitle()).thenReturn("Winnie-the-Pooh");
        when(work.getAuthorDisplay()).thenReturn("A. A. Milne");
        when(work.getPublicationYear()).thenReturn(1926);
        when(work.rightsFacts()).thenReturn(new RightsFacts(1926, List.of(AuthorFacts.of("A. A. Milne", 1882, 1956))));
        when(works.findById(id)).thenReturn(Optional.of(work));

        WorkUnlocksView view = service.unlocks(id);

        assertThat(view.readerCountry()).isEqualTo("DE");
        assertThat(view.readerFreeFrom()).isEqualTo(2027);
        assertThat(view.countries()).anySatisfy(unlock -> {
            assertThat(unlock.country()).isEqualTo("MX");
            assertThat(unlock.year()).isEqualTo(2057);
            assertThat(unlock.rule()).isEqualTo("Author's life + 100 years");
        });
    }

    @Test
    void aWorkOpenedByIdAnswersLikeASearchHitAndCountsAsDemand() {
        UUID id = UUID.randomUUID();
        CatalogWork work = mock(CatalogWork.class);
        when(work.getId()).thenReturn(id);
        when(work.getTitle()).thenReturn("Twilight");
        when(work.getAuthorDisplay()).thenReturn("Stephenie Meyer");
        when(work.getPublicationYear()).thenReturn(2005);
        when(work.getProductionStatus()).thenReturn(RightsStatus.PROTECTED);
        when(work.rightsFacts())
                .thenReturn(new RightsFacts(2005, List.of(AuthorFacts.of("Stephenie Meyer", 1973, null))));
        when(works.findById(id)).thenReturn(Optional.of(work));
        when(works.findAllById(List.of(id))).thenReturn(List.of(work));

        CatalogHit hit = service.work(id, null);

        assertThat(hit.title()).isEqualTo("Twilight");
        assertThat(hit.state()).isEqualTo(CatalogState.UNDER_COPYRIGHT);
        assertThat(hit.readerFreeFrom()).isNull();
        verify(store).logSearch(eq("twilight"), any(Hit.class), isNull(), startsWith("g:"), any(Instant.class));
    }

    static Hit hit(String title, String workSlug, RightsStatus status) {
        return new Hit(UUID.randomUUID(), title, null, null, 1900, "Someone", 10, workSlug, status, null, 1.0);
    }

    @Test
    void eachHitSaysWhatTheReaderCanDoWithIt() {
        when(store.search("prince", 20))
                .thenReturn(List.of(
                        hit("On the shelf", "readable", RightsStatus.FREE),
                        hit("Hidden here", "hidden", RightsStatus.FREE),
                        hit("Linked but withdrawn", "withdrawn", RightsStatus.FREE),
                        hit("Wantable", null, RightsStatus.UNKNOWN),
                        hit("Protected", null, RightsStatus.PROTECTED)));
        when(books.findByWorkSlug("readable")).thenReturn(List.of(new Book()));
        when(books.findByWorkSlug("hidden")).thenReturn(List.of(new Book()));
        when(books.findByWorkSlug("withdrawn")).thenReturn(List.of());
        when(readingRights.mayRead("readable")).thenReturn(true);
        when(readingRights.mayRead("hidden")).thenReturn(false);

        assertThat(service.search("Prince", 20))
                .extracting(CatalogHit::state)
                .containsExactly(
                        CatalogState.ON_SHELF,
                        CatalogState.NOT_IN_YOUR_COUNTRY,
                        CatalogState.WANTABLE,
                        CatalogState.WANTABLE,
                        CatalogState.UNDER_COPYRIGHT);
    }

    @Test
    void everySearchIsLoggedWithItsTopHitAndAnEmptyOneToo() {
        Hit top = hit("Lolita", null, RightsStatus.PROTECTED);
        when(store.search("lolita", 20)).thenReturn(List.of(top));
        when(store.search("zzqx", 20)).thenReturn(List.of());

        service.search("Lolita", 20);
        service.search("zzqx", 20);

        verify(store).logSearch(eq("lolita"), eq(top), isNull(), startsWith("g:"), any(Instant.class));
        verify(store).logSearch(eq("zzqx"), isNull(), isNull(), startsWith("g:"), any(Instant.class));
    }

    @Test
    void inALanguageEachHitSaysHowManyWantItAndWhetherTheReaderDoes() {
        UUID member = UUID.randomUUID();
        Hit popular = hit("Popular", null, RightsStatus.FREE);
        Hit lonely = hit("Lonely", null, RightsStatus.FREE);
        when(store.search("verne", 20)).thenReturn(List.of(popular, lonely));
        when(currentMember.id()).thenReturn(Optional.of(member));
        when(wants.readers(List.of(popular.id(), lonely.id()), Language.FR))
                .thenReturn(List.of(readers(popular.id(), 23), readers(lonely.id(), 1)));
        when(wants.wantedBy(member, List.of(popular.id(), lonely.id()), Language.FR))
                .thenReturn(List.of(lonely.id()));

        List<CatalogHit> hits = service.search("verne", 20, Language.FR);

        assertThat(hits.get(0).readers()).isEqualTo(23);
        assertThat(hits.get(0).wantedByMe()).isFalse();
        assertThat(hits.get(1).readers()).isNull();
        assertThat(hits.get(1).wantedByMe()).isTrue();
        verify(store).logSearch(eq("verne"), eq(popular), eq(member), eq("m:" + member), any(Instant.class));
    }

    static WantRepository.WorkReaders readers(UUID id, long count) {
        return new WantRepository.WorkReaders() {
            @Override
            public UUID getCatalogWorkId() {
                return id;
            }

            @Override
            public long getReaders() {
                return count;
            }
        };
    }

    @Test
    void publicDomainDayListsNextYearsWorksWithoutLoggingASearch() {
        Hit pooh = new Hit(
                UUID.randomUUID(),
                "Winnie-the-Pooh",
                null,
                "en",
                1926,
                "A. A. Milne",
                60,
                null,
                RightsStatus.PROTECTED,
                2027,
                0.0);
        CatalogWork work = mock(CatalogWork.class);
        when(work.getId()).thenReturn(pooh.id());
        when(work.rightsFacts()).thenReturn(new RightsFacts(1926, List.of(AuthorFacts.of("A. A. Milne", 1882, 1956))));
        when(store.freeFromCandidates(2027, CountryTerms.candidateWindow("DE", 2027), 200))
                .thenReturn(List.of(pooh));
        when(works.findAllById(List.of(pooh.id()))).thenReturn(List.of(work));

        assertThat(service.freeFrom(2027, 12, null))
                .extracting(CatalogHit::state, CatalogHit::readerCountry, CatalogHit::readerFreeFrom)
                .containsExactly(org.assertj.core.groups.Tuple.tuple(CatalogState.UNDER_COPYRIGHT, "DE", 2027));
        assertThat(service.currentYear()).isEqualTo(2026);
        verify(store, never()).logSearch(any(), any(), any(), any(), any());
    }

    @Test
    void publicDomainDayIsComputedDynamicallyForEachReadersCountry() {
        when(readerLocation.country()).thenReturn("US");
        when(readerLocation.detectedCountry()).thenReturn(Optional.of("US"));

        Hit brecht = new Hit(
                UUID.randomUUID(),
                "Mother Courage",
                null,
                "de",
                1949,
                "Bertolt Brecht",
                80,
                null,
                RightsStatus.PROTECTED,
                2027,
                0.0);
        Hit pooh = new Hit(
                UUID.randomUUID(),
                "Winnie-the-Pooh",
                null,
                "en",
                1926,
                "A. A. Milne",
                60,
                null,
                RightsStatus.PROTECTED,
                2027,
                0.0);
        Hit waves = new Hit(
                UUID.randomUUID(),
                "The Waves",
                null,
                "en",
                1931,
                "Virginia Woolf",
                40,
                null,
                RightsStatus.FREE,
                2012,
                0.0);

        CatalogWork brechtWork = mock(CatalogWork.class);
        when(brechtWork.getId()).thenReturn(brecht.id());
        when(brechtWork.rightsFacts())
                .thenReturn(new RightsFacts(1949, List.of(AuthorFacts.of("Bertolt Brecht", 1898, 1956))));

        CatalogWork poohWork = mock(CatalogWork.class);
        when(poohWork.getId()).thenReturn(pooh.id());
        when(poohWork.rightsFacts())
                .thenReturn(new RightsFacts(1926, List.of(AuthorFacts.of("A. A. Milne", 1882, 1956))));

        CatalogWork wavesWork = mock(CatalogWork.class);
        when(wavesWork.getId()).thenReturn(waves.id());
        when(wavesWork.rightsFacts())
                .thenReturn(new RightsFacts(1931, List.of(AuthorFacts.of("Virginia Woolf", 1882, 1941))));

        when(store.freeFromCandidates(2027, CountryTerms.candidateWindow("US", 2027), 200))
                .thenReturn(List.of(brecht, pooh, waves));
        when(works.findAllById(List.of(brecht.id(), pooh.id(), waves.id())))
                .thenReturn(List.of(brechtWork, poohWork, wavesWork));

        // Brecht (1949) is protected in the US until 2045; Pooh (1926, d. 1956) and The Waves (1931, d. 1941) both
        // unlock for US readers on 1 January 2027.
        assertThat(service.freeFrom(2027, 12, null))
                .extracting(CatalogHit::title, CatalogHit::readerCountry, CatalogHit::readerFreeFrom)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple("Winnie-the-Pooh", "US", 2027),
                        org.assertj.core.groups.Tuple.tuple("The Waves", "US", 2027));
    }

    @Test
    void anUnplacedIpDoesNotClaimTheFallbackCountry() {
        when(readerLocation.country()).thenReturn("DE");
        when(readerLocation.detectedCountry()).thenReturn(Optional.empty());

        Hit pooh = new Hit(
                UUID.randomUUID(),
                "Winnie-the-Pooh",
                null,
                "en",
                1926,
                "A. A. Milne",
                60,
                null,
                RightsStatus.PROTECTED,
                2027,
                0.0);
        CatalogWork work = mock(CatalogWork.class);
        when(work.getId()).thenReturn(pooh.id());
        when(work.rightsFacts()).thenReturn(new RightsFacts(1926, List.of(AuthorFacts.of("A. A. Milne", 1882, 1956))));
        when(store.freeFromCandidates(2027, CountryTerms.candidateWindow("DE", 2027), 200))
                .thenReturn(List.of(pooh));
        when(works.findAllById(List.of(pooh.id()))).thenReturn(List.of(work));

        List<CatalogHit> hits = service.freeFrom(2027, 12, null);

        assertThat(hits).hasSize(1);
        assertThat(hits.getFirst().readerCountry()).isNull();
        assertThat(hits.getFirst().readerFreeFrom()).isEqualTo(2027);
    }

    @Test
    void aQueryTooShortToFoldToTwoCharactersFindsNothing() {
        assertThat(service.search(" ! ", 20)).isEmpty();
        assertThat(service.search("a", 20)).isEmpty();
    }
}
