package com.almonium.learning.wanted.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.catalog.dto.response.TextSourceView;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.catalog.service.TextSources;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.wanted.dto.request.FactoryReservationRequest;
import com.almonium.learning.wanted.dto.response.FactoryQueueRow;
import com.almonium.learning.wanted.model.entity.Preparation;
import com.almonium.learning.wanted.model.entity.Want;
import com.almonium.learning.wanted.model.enums.WantStatus;
import com.almonium.learning.wanted.repository.WantRepository;
import com.almonium.learning.wanted.repository.WantRepository.WantGroup;
import com.almonium.user.core.exception.ResourceConflictException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FactoryServiceTest {
    WantRepository wants = mock(WantRepository.class);
    CatalogWorkRepository works = mock(CatalogWorkRepository.class);
    TextSources sources = mock(TextSources.class);
    PreparationService preparations = mock(PreparationService.class);
    FactoryService service = new FactoryService(
            wants, works, sources, preparations, Clock.fixed(Instant.parse("2026-10-11T00:00:00Z"), ZoneOffset.UTC));

    CatalogWork dracula = work("Dracula", "en");
    CatalogWork faust = work("Faust", "de");

    @BeforeEach
    void setUp() {
        when(works.findAllById(any())).thenReturn(List.of(dracula, faust));
        when(preparations.forWorks(any())).thenReturn(List.of());
    }

    @Test
    void ranksOpenWantsByReadersAndOffersEachTheBestFreeText() {
        WantGroup draculaInUkrainian = group(dracula, Language.UK, 2, "2026-10-01T00:00:00Z");
        WantGroup faustInGerman = group(faust, Language.DE, 5, "2026-10-02T00:00:00Z");
        when(wants.groups(List.of(WantStatus.OPEN))).thenReturn(List.of(draculaInUkrainian, faustInGerman));
        when(sources.forWork(dracula.getId()))
                .thenReturn(List.of(
                        source("en", RightsStatus.FREE, "https://www.gutenberg.org/ebooks/345"),
                        source("uk", RightsStatus.UNKNOWN, "https://uk.wikisource.org/wiki/Dracula")));
        when(sources.forWork(faust.getId()))
                .thenReturn(List.of(
                        source("en", RightsStatus.FREE, "https://www.gutenberg.org/ebooks/14591"),
                        source("de", RightsStatus.FREE, "https://www.gutenberg.org/ebooks/2229")));

        List<FactoryQueueRow> queue = service.queue();

        assertThat(queue).extracting(FactoryQueueRow::title).containsExactly("Faust", "Dracula");
        // Faust wanted in German: the free German text, not the English translation that sorts first
        assertThat(queue.get(0).source().language()).isEqualTo("de");
        assertThat(queue.get(0).readers()).isEqualTo(5);
        // Dracula wanted in Ukrainian: no free Ukrainian text, so the original to translate from
        assertThat(queue.get(1).source().pageUrl()).isEqualTo("https://www.gutenberg.org/ebooks/345");
        assertThat(queue.get(1).workLanguage()).isEqualTo("en");
    }

    @Test
    void leavesOutWhatIsBeingPreparedAndWhatHasNoFreeText() {
        WantGroup draculaInEnglish = group(dracula, Language.EN, 1, "2026-10-01T00:00:00Z");
        WantGroup faustInGerman = group(faust, Language.DE, 1, "2026-10-01T00:00:00Z");
        when(wants.groups(List.of(WantStatus.OPEN))).thenReturn(List.of(draculaInEnglish, faustInGerman));
        Preparation preparing = new Preparation();
        preparing.setCatalogWork(dracula);
        preparing.setLanguage(Language.EN);
        when(preparations.forWorks(any())).thenReturn(List.of(preparing));
        when(sources.forWork(faust.getId()))
                .thenReturn(List.of(source("de", RightsStatus.PROTECTED, "https://www.gutenberg.org/ebooks/1")));

        List<FactoryQueueRow> queue = service.queue();

        assertThat(queue).extracting(FactoryQueueRow::title).containsExactly("Faust");
        assertThat(queue.get(0).source()).isNull();
    }

    @Test
    void aReservationMovesTheWantsToInProgressWithNobodyDeciding() {
        UUID sourceId = UUID.randomUUID();
        Preparation reserved = new Preparation();
        reserved.setId(UUID.randomUUID());
        when(preparations.reserve(dracula.getId(), Language.UK, sourceId)).thenReturn(reserved);
        Want want = new Want();
        want.setStatus(WantStatus.OPEN);
        when(wants.findByCatalogWorkIdAndLanguageAndStatusIn(dracula.getId(), Language.UK, WantStatus.STANDING))
                .thenReturn(List.of(want));

        var reservation = service.reserve(new FactoryReservationRequest(dracula.getId(), Language.UK, sourceId));

        assertThat(reservation.preparationId()).isEqualTo(reserved.getId());
        assertThat(want.getStatus()).isEqualTo(WantStatus.IN_PROGRESS);
        assertThat(want.getDecidedBy()).isNull();
        assertThat(want.getDecidedAt()).isEqualTo(Instant.parse("2026-10-11T00:00:00Z"));
    }

    @Test
    void aReservationNobodyWaitsOnIsRefused() {
        UUID sourceId = UUID.randomUUID();
        when(preparations.reserve(any(), any(), any())).thenReturn(new Preparation());
        when(wants.findByCatalogWorkIdAndLanguageAndStatusIn(any(), any(), any()))
                .thenReturn(List.of());

        assertThatThrownBy(() -> service.reserve(new FactoryReservationRequest(dracula.getId(), Language.UK, sourceId)))
                .isInstanceOf(ResourceConflictException.class);
    }

    static CatalogWork work(String title, String language) {
        CatalogWork work = new CatalogWork();
        work.setId(UUID.randomUUID());
        work.setTitle(title);
        work.setAuthorDisplay("Author");
        work.setOriginalLanguage(language);
        work.setPublicationYear(1897);
        return work;
    }

    static WantGroup group(CatalogWork work, Language language, long readers, String since) {
        WantGroup group = mock(WantGroup.class);
        when(group.getCatalogWorkId()).thenReturn(work.getId());
        when(group.getLanguage()).thenReturn(language);
        when(group.getReaders()).thenReturn(readers);
        when(group.getFirstWantedAt()).thenReturn(Instant.parse(since));
        return group;
    }

    static TextSourceView source(String language, RightsStatus status, String pageUrl) {
        return new TextSourceView(
                UUID.randomUUID(),
                "GUTENBERG",
                language,
                "Label",
                false,
                List.of(),
                true,
                pageUrl,
                pageUrl + ".epub3.images",
                status,
                null,
                null);
    }
}
