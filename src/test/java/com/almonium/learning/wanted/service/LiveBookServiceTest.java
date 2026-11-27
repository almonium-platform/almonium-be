package com.almonium.learning.wanted.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.dto.response.TextSourceView;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.catalog.service.TextSources;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.service.ReaderLocation;
import com.almonium.learning.rights.service.ReadingRights;
import com.almonium.learning.rights.service.UnlockDates;
import com.almonium.learning.wanted.dto.request.LiveBookRequest;
import com.almonium.learning.wanted.dto.response.LiveBook;
import com.almonium.learning.wanted.model.entity.Preparation;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

class LiveBookServiceTest {
    CatalogWorkRepository works = mock(CatalogWorkRepository.class);
    BookRepository books = mock(BookRepository.class);
    TextSources sources = mock(TextSources.class);
    PreparationService preparations = mock(PreparationService.class);
    ReadingRights readingRights = mock(ReadingRights.class);
    UnlockDates unlockDates = mock(UnlockDates.class);
    ReaderLocation readerLocation = mock(ReaderLocation.class);
    LiveBookLimiter limiter = mock(LiveBookLimiter.class);
    LiveBookService service = new LiveBookService(
            works, books, sources, preparations, readingRights, unlockDates, readerLocation, limiter);

    CatalogWork work = new CatalogWork();
    User reader = new User();

    TextSourceView wikisource = text("WIKISOURCE", "en", false, RightsStatus.FREE);
    TextSourceView standard = text("STANDARD_EBOOKS", "en", false, RightsStatus.FREE);
    TextSourceView gutenberg = text("GUTENBERG", "en", false, RightsStatus.FREE);
    TextSourceView german = text("GUTENBERG", "de", true, RightsStatus.FREE);
    TextSourceView eltec = text("ELTEC", "en", false, RightsStatus.FREE);
    TextSourceView unknown = text("GUTENBERG", "en", false, RightsStatus.UNKNOWN);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "maxAtOnce", 4);
        work.setId(UUID.randomUUID());
        work.setTitle("Dracula");
        work.setPublicationYear(1897);
        work.setProductionStatus(RightsStatus.FREE);
        reader.setId(UUID.randomUUID());
        when(works.findById(work.getId())).thenReturn(Optional.of(work));
        // the catalogue's order: a page about the work first, here, to show that a ready file is preferred
        when(sources.forWork(work.getId()))
                .thenReturn(List.of(wikisource, eltec, standard, unknown, gutenberg, german));
        when(readerLocation.country()).thenReturn("DE");
        when(unlockDates.in(any(), any())).thenReturn(new UnlockDates.Unlock("DE", RightsStatus.FREE, null, null));
        when(preparations.find(any(), any())).thenReturn(Optional.empty());
        when(limiter.tryAcquire(reader.getId())).thenReturn(true);
    }

    static TextSourceView text(String kind, String language, boolean translation, RightsStatus status) {
        String address = "https://example.test/" + kind + "/" + language + "/" + status;
        return new TextSourceView(
                UUID.randomUUID(),
                kind,
                language,
                kind,
                translation,
                List.of(),
                true,
                address,
                address + ".epub",
                status,
                null,
                null);
    }

    Preparation preparation(String reporter, String phase, Integer progress) {
        Preparation preparation = new Preparation();
        preparation.setId(UUID.randomUUID());
        preparation.setCatalogWork(work);
        preparation.setLanguage(Language.EN);
        preparation.setReporter(reporter);
        preparation.setPhase(phase);
        preparation.setProgress(progress);
        when(preparations.watch(preparation.getId())).thenReturn(Optional.of(preparation));
        return preparation;
    }

    @Test
    void theOriginalsFreeTextIsFetchedFromALibraryThatHasItReadyWithTheOthersStandingBy() {
        Preparation started = preparation(PreparationService.REPORTER_LIVE, "downloading", 0);
        when(preparations.startLive(reader, work.getId(), standard, List.of(gutenberg, wikisource)))
                .thenReturn(started);

        LiveBook book = service.open(reader, new LiveBookRequest(work.getId(), null));

        assertThat(book.state()).isEqualTo(LiveBook.State.ARRIVING);
        assertThat(book.step()).isEqualTo(LiveBook.Step.FETCHING);
        assertThat(book.preparationId()).isEqualTo(started.getId());
        assertThat(book.language()).isEqualTo(Language.EN);
    }

    @Test
    void aTextTheReaderChoseStandsAlone() {
        Preparation started = preparation(PreparationService.REPORTER_LIVE, "downloading", 0);
        when(preparations.startLive(reader, work.getId(), german, List.of())).thenReturn(started);

        LiveBook book = service.open(reader, new LiveBookRequest(work.getId(), german.downloadUrl()));

        assertThat(book.state()).isEqualTo(LiveBook.State.ARRIVING);
        // a text whose rights are not settled, or one the processor cannot fetch, is not opened on a reader's word
        for (TextSourceView refused : List.of(unknown, eltec)) {
            assertThatThrownBy(() -> service.open(reader, new LiveBookRequest(work.getId(), refused.downloadUrl())))
                    .isInstanceOf(ResourceConflictException.class);
        }
    }

    @Test
    void aBookOnTheShelfIsOpenedAsItIs() {
        work.setWorkSlug("dracula");
        Book adapted = new Book();
        adapted.setEditionSlug("dracula-en-b1");
        adapted.setLanguage(Language.EN);
        adapted.setEditionType("adaptation");
        Book original = new Book();
        original.setEditionSlug("dracula-en-original");
        original.setLanguage(Language.EN);
        original.setEditionType("original");
        when(books.findByWorkSlug("dracula")).thenReturn(List.of(adapted, original));
        when(readingRights.mayRead("dracula")).thenReturn(true);

        LiveBook book = service.open(reader, new LiveBookRequest(work.getId(), null));

        assertThat(book.state()).isEqualTo(LiveBook.State.READY);
        assertThat(book.editionSlug()).isEqualTo("dracula-en-original");
        verify(preparations, never()).startLive(any(), any(), any(), any());
    }

    @Test
    void aSecondReaderJoinsTheFirstOnesWaitAndAFactoryBookIsSaidToBeOnItsWay() {
        Preparation arriving = preparation(PreparationService.REPORTER_LIVE, "ingesting", 1);
        when(preparations.find(work.getId(), Language.EN)).thenReturn(Optional.of(arriving));

        LiveBook joined = service.open(reader, new LiveBookRequest(work.getId(), null));

        assertThat(joined.state()).isEqualTo(LiveBook.State.ARRIVING);
        assertThat(joined.step()).isEqualTo(LiveBook.Step.SENTENCES);
        verify(limiter, never()).tryAcquire(any());

        arriving.setReporter(PreparationService.REPORTER_FACTORY);
        assertThat(service.open(reader, new LiveBookRequest(work.getId(), null)).state())
                .isEqualTo(LiveBook.State.BEING_PREPARED);

        arriving.setReporter(PreparationService.REPORTER_LIVE);
        arriving.setPhase("published");
        arriving.setEditionSlug("dracula-en-original");
        LiveBook ready = service.status(arriving.getId());
        assertThat(ready.state()).isEqualTo(LiveBook.State.READY);
        assertThat(ready.editionSlug()).isEqualTo("dracula-en-original");
    }

    @Test
    void aFailedArrivalSaysWhyAndIsStartedOverWhenAskedAgain() {
        Preparation failed = preparation(PreparationService.REPORTER_LIVE, "failed", 0);
        failed.setError("www.gutenberg.org answered HTTP 503.");
        when(preparations.find(work.getId(), Language.EN)).thenReturn(Optional.of(failed));
        Preparation again = preparation(PreparationService.REPORTER_LIVE, "downloading", 0);
        when(preparations.startLive(any(), any(), any(), any())).thenReturn(again);

        LiveBook status = service.status(failed.getId());
        assertThat(status.state()).isEqualTo(LiveBook.State.FAILED);
        assertThat(status.error()).isEqualTo("www.gutenberg.org answered HTTP 503.");

        assertThat(service.open(reader, new LiveBookRequest(work.getId(), null)).state())
                .isEqualTo(LiveBook.State.ARRIVING);
    }

    @Test
    void aWorkStillProtectedWhereTheReaderIsIsNotFetchedForThem() {
        when(unlockDates.in(any(), any())).thenReturn(new UnlockDates.Unlock("MX", RightsStatus.PROTECTED, 2040, null));

        assertThatThrownBy(() -> service.open(reader, new LiveBookRequest(work.getId(), null)))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("your country");
        verify(preparations, never()).startLive(any(), any(), any(), any());
    }

    @Test
    void aWorkWithNoFreeTextToFetchOrNoYearIsLeftToBeWanted() {
        when(sources.forWork(work.getId())).thenReturn(List.of(eltec, unknown, german));
        assertThatThrownBy(() -> service.open(reader, new LiveBookRequest(work.getId(), null)))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("Want it instead");

        when(sources.forWork(work.getId())).thenReturn(List.of(gutenberg));
        work.setPublicationYear(null);
        assertThatThrownBy(() -> service.open(reader, new LiveBookRequest(work.getId(), null)))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("year of publication");
    }

    @Test
    void oneBookAtATimeAFewAtOnceAndAHandfulADay() {
        when(preparations.arrivingFor(reader, LiveBookService.ARRIVAL)).thenReturn(true);
        assertTooMany("One book at a time");

        when(preparations.arrivingFor(reader, LiveBookService.ARRIVAL)).thenReturn(false);
        when(preparations.arriving(LiveBookService.ARRIVAL)).thenReturn(4L);
        assertTooMany("Several books are arriving");

        when(preparations.arriving(LiveBookService.ARRIVAL)).thenReturn(3L);
        when(limiter.tryAcquire(reader.getId())).thenReturn(false);
        assertTooMany("today’s share");
        verify(preparations, never()).startLive(any(), any(), any(), any());
    }

    @Test
    void aBookThatCouldNotBeStartedGivesTheReaderTheirPlaceBack() {
        when(preparations.startLive(any(), any(), any(), any())).thenThrow(new IllegalStateException("processor down"));

        assertThatThrownBy(() -> service.open(reader, new LiveBookRequest(work.getId(), null)))
                .isInstanceOf(IllegalStateException.class);
        verify(limiter).release(reader.getId());
    }

    @Test
    void theStepsFollowTheProcessorsStages() {
        assertThat(LiveBookService.step("downloading", 0)).isEqualTo(LiveBook.Step.FETCHING);
        assertThat(LiveBookService.step("ingesting", null)).isEqualTo(LiveBook.Step.CHAPTERS);
        assertThat(LiveBookService.step("ingesting", 1)).isEqualTo(LiveBook.Step.SENTENCES);
        assertThat(LiveBookService.step("ready", 2)).isEqualTo(LiveBook.Step.SHELVING);
    }

    private void assertTooMany(String message) {
        assertThatThrownBy(() -> service.open(reader, new LiveBookRequest(work.getId(), null)))
                .isInstanceOfSatisfying(ResponseStatusException.class, exception -> {
                    assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
                    assertThat(exception.getReason()).contains(message);
                });
    }
}
