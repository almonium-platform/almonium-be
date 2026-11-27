package com.almonium.learning.wanted.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
import com.almonium.user.core.model.entity.User;
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
                null,
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

    @Test
    void aLiveStartTellsTheProcessorAReaderIsWaitingAndWhichTextsStandBy() {
        TextSourceView standard = source(RightsStatus.FREE, "en");
        TextSourceView gutenberg = source(RightsStatus.FREE, "en");
        User reader = new User();
        reader.setId(UUID.randomUUID());
        when(processor.startSourceIngest(
                        any(),
                        any(BookProcessorClient.SourceText.class),
                        any(),
                        any(),
                        any(),
                        any(),
                        anyBoolean(),
                        any()))
                .thenReturn(new ProcessorIngestStatus(
                        edition, "pending-1", "queued", "downloading", 0, "", null, null, List.of(), null));

        Preparation preparation = service.startLive(reader, work.getId(), standard, List.of(gutenberg));

        assertThat(preparation.getReporter()).isEqualTo(PreparationService.REPORTER_LIVE);
        assertThat(preparation.getStartedBy()).isSameAs(reader);
        assertThat(preparation.getLanguage()).isEqualTo(Language.EN);
        assertThat(preparation.getCreatedAt()).isEqualTo(Instant.parse("2026-10-08T00:00:00Z"));
        assertThat(preparation.getEditionSlug()).isEqualTo("pending-1");
        BookProcessorClient.SourceText text =
                new BookProcessorClient.SourceText(standard.downloadUrl(), "gutenberg", standard.pageUrl());
        verify(processor)
                .startSourceIngest(
                        any(),
                        eq(text),
                        eq("Dracula"),
                        eq("Bram Stoker"),
                        eq(Language.EN),
                        eq(1897),
                        eq(true),
                        eq(List.of(text)));
    }

    @Test
    void watchingAsksTheProcessorUnlessItWasAskedWithinTheSecondOrTheBookIsFinished() {
        Instant now = Instant.parse("2026-10-08T00:00:00Z");
        Preparation preparation = new Preparation();
        preparation.setId(UUID.randomUUID());
        preparation.setReporter(PreparationService.REPORTER_LIVE);
        preparation.setProcessorEditionId(edition);
        preparation.setPhase("ingesting");
        preparation.setCheckedAt(now.minusSeconds(2));
        preparation.setSourceKind("GUTENBERG");
        when(preparations.findById(preparation.getId())).thenReturn(Optional.of(preparation));
        when(processor.sourceIngestStatus(edition))
                .thenReturn(new ProcessorIngestStatus(
                        edition,
                        "dracula-en-original",
                        "published",
                        "published",
                        100,
                        "",
                        now,
                        UUID.randomUUID(),
                        List.of("ingest", "sentences", "publish"),
                        null,
                        "standard_ebooks",
                        "https://standardebooks.org/ebooks/bram-stoker/dracula"));

        Preparation watched = service.watch(preparation.getId()).orElseThrow();

        assertThat(watched.getPhase()).isEqualTo("published");
        assertThat(watched.getEditionSlug()).isEqualTo("dracula-en-original");
        assertThat(watched.getProgress()).isEqualTo(4);
        // the processor fell back on another library's text, and the preparation says so
        assertThat(watched.getSourceKind()).isEqualTo("STANDARD_EBOOKS");
        assertThat(watched.getSourceReference()).isEqualTo("https://standardebooks.org/ebooks/bram-stoker/dracula");

        service.watch(preparation.getId());
        verify(processor, times(1)).sourceIngestStatus(edition);
    }

    @Test
    void aProcessorThatDoesNotAnswerLeavesTheLastWordStanding() {
        Preparation preparation = new Preparation();
        preparation.setId(UUID.randomUUID());
        preparation.setReporter(PreparationService.REPORTER_LIVE);
        preparation.setProcessorEditionId(edition);
        preparation.setPhase("ingesting");
        when(preparations.findById(preparation.getId())).thenReturn(Optional.of(preparation));
        when(processor.sourceIngestStatus(edition)).thenThrow(new IllegalStateException("down"));

        assertThat(service.watch(preparation.getId()).orElseThrow().getPhase()).isEqualTo("ingesting");
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
        when(preparations.findByReporterInAndProcessorEditionIdNotNullAndPhaseNotIn(
                        List.of(PreparationService.REPORTER_PROCESSOR, PreparationService.REPORTER_LIVE),
                        PreparationService.FINISHED))
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
