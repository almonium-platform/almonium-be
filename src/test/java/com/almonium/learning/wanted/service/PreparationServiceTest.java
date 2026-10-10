package com.almonium.learning.wanted.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.response.ProcessorIngestStatus;
import com.almonium.learning.book.service.BookProcessorClient;
import com.almonium.learning.catalog.dto.response.TextSourceView;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.catalog.service.TextSources;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.wanted.model.entity.Preparation;
import com.almonium.learning.wanted.repository.PreparationRepository;
import com.almonium.user.core.exception.ResourceConflictException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PreparationServiceTest {
    PreparationRepository preparations = mock(PreparationRepository.class);
    CatalogWorkRepository works = mock(CatalogWorkRepository.class);
    TextSources sources = mock(TextSources.class);
    BookProcessorClient processor = mock(BookProcessorClient.class);
    PreparationService service = new PreparationService(
            preparations,
            works,
            sources,
            processor,
            Clock.fixed(Instant.parse("2026-10-08T00:00:00Z"), ZoneOffset.UTC));

    CatalogWork work;
    UUID edition = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        work = new CatalogWork();
        work.setId(UUID.randomUUID());
        work.setTitle("Dracula");
        work.setAuthorDisplay("Bram Stoker");
        work.setPublicationYear(1897);
        when(works.findById(work.getId())).thenReturn(Optional.of(work));
        when(preparations.saveAndFlush(any(Preparation.class))).thenAnswer(call -> {
            Preparation preparation = call.getArgument(0);
            if (preparation.getId() == null) {
                preparation.setId(UUID.randomUUID());
            }
            return preparation;
        });
        when(preparations.findByCatalogWorkIdAndLanguage(any(), any())).thenReturn(Optional.empty());
        when(preparations.save(any(Preparation.class))).thenAnswer(call -> call.getArgument(0));
    }

    TextSourceView source(RightsStatus status, String language) {
        TextSourceView view = new TextSourceView(
                UUID.randomUUID(),
                "GUTENBERG",
                language,
                "Dracula",
                false,
                List.of(),
                true,
                "https://www.gutenberg.org/ebooks/345",
                "https://www.gutenberg.org/ebooks/345.epub3.images",
                status,
                null);
        when(sources.find(view.id())).thenReturn(Optional.of(view));
        when(sources.belongsTo(view, work.getId())).thenReturn(true);
        return view;
    }

    @Test
    void startingSendsTheProcessorTheDownloadAndKeepsItsProgress() {
        TextSourceView dracula = source(RightsStatus.FREE, "en");
        when(processor.startSourceIngest(
                        any(),
                        eq(dracula.downloadUrl()),
                        eq("gutenberg"),
                        eq(dracula.pageUrl()),
                        eq("Dracula"),
                        eq("Bram Stoker"),
                        eq(Language.EN),
                        eq(1897)))
                .thenReturn(new ProcessorIngestStatus(
                        edition, "dracula", "queued", "downloading", 0, "", null, null, List.of(), null));

        Preparation preparation = service.start(null, work.getId(), Language.DE, dracula.id());

        assertThat(preparation.getProcessorEditionId()).isEqualTo(edition);
        assertThat(preparation.getPhase()).isEqualTo("downloading");
        assertThat(preparation.getProgress()).isZero();
        assertThat(preparation.getLanguage()).isEqualTo(Language.DE);
        assertThat(preparation.getSourceLanguage()).isEqualTo("en");
    }

    @Test
    void aProtectedTextOrALanguageTheProcessorCannotReadIsRefused() {
        TextSourceView styles = source(RightsStatus.PROTECTED, "en");
        TextSourceView klingon = source(RightsStatus.FREE, "tlh");

        assertThatThrownBy(() -> service.start(null, work.getId(), Language.EN, styles.id()))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("under copyright");
        assertThatThrownBy(() -> service.start(null, work.getId(), Language.EN, klingon.id()))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("does not read tlh");
        verify(processor, never()).startSourceIngest(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void theFactoryReservesWithoutAskingTheProcessorAndPushesItsOwnProgress() {
        TextSourceView dracula = source(RightsStatus.FREE, "en");

        Preparation reserved = service.reserve(work.getId(), Language.UK, dracula.id());

        assertThat(reserved.getReporter()).isEqualTo(PreparationService.REPORTER_FACTORY);
        assertThat(reserved.getPhase()).isEqualTo("queued");
        assertThat(reserved.getStartedBy()).isNull();
        verify(processor, never()).startSourceIngest(any(), any(), any(), any(), any(), any(), any(), any());

        when(preparations.findById(reserved.getId())).thenReturn(Optional.of(reserved));
        Preparation reported = service.report(
                reserved.getId(),
                new ProcessorIngestStatus(
                        edition, "dracula-en", "ready", "producing", 100, "", null, null, List.of("ingest"), "adapt"));
        assertThat(reported.getPhase()).isEqualTo("producing");
        assertThat(reported.getProcessorEditionId()).isEqualTo(edition);
        assertThat(reported.getProgress()).isEqualTo(1);
        // a preparation /ops started is not the factory's to report on
        reserved.setReporter(PreparationService.REPORTER_PROCESSOR);
        assertThatThrownBy(() -> service.report(reserved.getId(), reported(edition)))
                .isInstanceOf(jakarta.persistence.EntityNotFoundException.class);
    }

    private static ProcessorIngestStatus reported(UUID edition) {
        return new ProcessorIngestStatus(edition, "dracula-en", "ready", "ready", 100, "", null, null);
    }

    @Test
    void refreshingFollowsTheProcessorThroughItsStages() {
        Preparation preparation = new Preparation();
        preparation.setId(UUID.randomUUID());
        preparation.setProcessorEditionId(edition);
        preparation.setPhase("ingesting");
        when(preparations.findByReporterAndProcessorEditionIdNotNullAndPhaseNotIn(
                        PreparationService.REPORTER_PROCESSOR, PreparationService.FINISHED))
                .thenReturn(List.of(preparation));
        when(processor.sourceIngestStatus(edition))
                .thenReturn(new ProcessorIngestStatus(
                        edition,
                        "dracula",
                        "processing",
                        "ingesting",
                        60,
                        "",
                        null,
                        null,
                        List.of("ingest", "metadata", "chapter_analysis"),
                        "lexical"));

        service.refresh();

        assertThat(preparation.getProgress()).isEqualTo(2);
        assertThat(preparation.getStage()).isEqualTo("lexical");
    }
}
