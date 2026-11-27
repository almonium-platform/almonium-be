package com.almonium.learning.cover.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.config.properties.CoverProperties;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.cover.client.CommonsClient;
import com.almonium.learning.cover.client.CommonsClient.CommonsFile;
import com.almonium.learning.cover.client.CoverJudge;
import com.almonium.learning.cover.model.CoverStatus;
import com.almonium.learning.cover.model.WorkCover;
import com.almonium.learning.cover.repository.WorkCoverRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CoverServiceTest {
    static final Clock CLOCK = Clock.fixed(Instant.parse("2026-11-27T00:00:00Z"), ZoneOffset.UTC);

    WorkCoverRepository covers = mock(WorkCoverRepository.class);
    BookRepository books = mock(BookRepository.class);
    CatalogWorkRepository works = mock(CatalogWorkRepository.class);
    CommonsClient commons = mock(CommonsClient.class);
    CoverJudge judge = mock(CoverJudge.class);
    CoverProperties properties = new CoverProperties();
    CoverService service = new CoverService(covers, books, works, commons, judge, properties, CLOCK);

    @BeforeEach
    void aBookLinkedToTheCatalogue() {
        properties.setApiKey("key");
        Book book = new Book();
        book.setTitle("Frankenstein");
        book.setAuthor("Mary Shelley");
        when(books.findByWorkSlug("frankenstein")).thenReturn(List.of(book));
        CatalogWork work = mock(CatalogWork.class);
        when(work.getWikidataId()).thenReturn("Q150827");
        when(works.findByWorkSlug("frankenstein")).thenReturn(List.of(work));
        when(covers.findById("frankenstein")).thenReturn(Optional.empty());
    }

    static CommonsFile file(String title, String mime, boolean copyrighted, String... categories) {
        return new CommonsFile(
                title,
                "https://commons.wikimedia.org/wiki/" + title,
                "https://upload.wikimedia.org/" + title,
                mime,
                1200,
                1800,
                "Theodor von Holst",
                "Public domain",
                copyrighted,
                List.of(categories));
    }

    WorkCover saved() {
        ArgumentCaptor<WorkCover> captor = ArgumentCaptor.forClass(WorkCover.class);
        verify(covers).save(captor.capture());
        return captor.getValue();
    }

    @Test
    void onlyFilesThatPassTheLicenceRuleAreShownAndThePickIsKeptWithItsRecord() {
        when(commons.filesFor("Q150827"))
                .thenReturn(List.of(
                        file("File:Scan.djvu", "image/vnd.djvu", false, "PD-old-100-expired"),
                        file("File:Poster 1931.jpg", "image/jpeg", true, "PD US"),
                        file("File:Recent.jpg", "image/jpeg", false, "PD-old-70-expired"),
                        file("File:Title page.jpg", "image/jpeg", false, "PD-old-100-expired"),
                        file("File:Frontispiece 1831.jpg", "image/jpeg", false, "PD-Art (PD-old-100)")));
        when(commons.download(any())).thenReturn(new byte[] {1, 2, 3});
        when(judge.choose(eq("Frankenstein"), eq("Mary Shelley"), anyList()))
                .thenReturn(new CoverJudge.Verdict(1, 50, 20, "The creature and its maker.", "sonnet"));

        assertThat(service.prepare("frankenstein")).isEqualTo(CoverStatus.APPROVED);

        ArgumentCaptor<List<CoverJudge.Shown>> shown = ArgumentCaptor.captor();
        verify(judge).choose(any(), any(), shown.capture());
        assertThat(shown.getValue())
                .extracting(CoverJudge.Shown::title)
                .containsExactly("File:Title page.jpg", "File:Frontispiece 1831.jpg");
        WorkCover cover = saved();
        assertThat(cover.getStatus()).isEqualTo(CoverStatus.APPROVED);
        assertThat(cover.getImage()).containsExactly(1, 2, 3);
        assertThat(cover.getSourcePageUrl()).endsWith("File:Frontispiece 1831.jpg");
        assertThat(cover.getLicenceBasis()).isEqualTo("PD-Art (PD-old-100)");
        assertThat(cover.getFocusY()).isEqualTo(20);
        assertThat(cover.getArtist()).isEqualTo("Theodor von Holst");
    }

    @Test
    void aWorkWithNothingFreeToTakeNeverReachesTheModel() {
        when(commons.filesFor("Q150827"))
                .thenReturn(List.of(file("File:Poster 1931.jpg", "image/jpeg", true, "PD US")));

        assertThat(service.prepare("frankenstein")).isEqualTo(CoverStatus.NONE);

        verify(judge, never()).choose(any(), any(), anyList());
        assertThat(saved().getImage()).isNull();
    }

    @Test
    void theModelMayChooseNothingAndTheBookKeepsItsTypographicCover() {
        when(commons.filesFor("Q150827"))
                .thenReturn(List.of(file("File:Title page.jpg", "image/jpeg", false, "PD-old-100-expired")));
        when(commons.download(any())).thenReturn(new byte[] {1});
        when(judge.choose(any(), any(), anyList()))
                .thenReturn(new CoverJudge.Verdict(-1, 50, 50, "Only a title page.", "sonnet"));

        assertThat(service.prepare("frankenstein")).isEqualTo(CoverStatus.NONE);

        WorkCover cover = saved();
        assertThat(cover.getImage()).isNull();
        assertThat(cover.getReason()).isEqualTo("Only a title page.");
    }

    @Test
    void withoutAKeyNothingIsLookedFor() {
        properties.setApiKey("");

        assertThat(service.prepareMissing()).isZero();

        verify(commons, never()).filesFor(any());
    }
}
