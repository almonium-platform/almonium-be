package com.almonium.learning.lexicon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.properties.SenseComparisonProperties;
import com.almonium.learning.almo.client.OpenAiChatClient;
import com.almonium.learning.lexicon.dto.request.SenseComparisonRequest;
import com.almonium.learning.lexicon.dto.response.SenseComparisonResponse;
import com.almonium.learning.lexicon.repository.SenseComparisonStore;
import com.almonium.learning.lexicon.repository.SenseComparisonStore.NewComparison;
import com.almonium.learning.lexicon.repository.SenseComparisonStore.SenseFacts;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class SenseComparisonServiceTest {
    static final UUID BIG = UUID.fromString("00000000-0000-0000-0000-000000000001");
    static final UUID LARGE = UUID.fromString("00000000-0000-0000-0000-000000000002");
    static final Instant NOW = Instant.parse("2026-11-25T12:00:00Z");

    @Mock
    private SenseComparisonStore store;

    @Mock
    private OpenAiChatClient model;

    private final User user = User.builder().id(UUID.randomUUID()).build();
    private SenseComparisonService service;

    @BeforeEach
    void setUp() {
        service = new SenseComparisonService(
                store, model, new SenseComparisonProperties(), new ObjectMapper(), Clock.fixed(NOW, ZoneOffset.UTC));
    }

    @Test
    void buysAnExplanationFromTheLexiconsFactsAndRecordsWhatItCost() {
        known();
        when(store.summary(anyString())).thenReturn(Optional.empty());
        when(model.complete(anyString(), any()))
                .thenReturn(new OpenAiChatClient.Completion(
                        "{\"summary\": \" Big is the everyday word. \"}", "gpt-4.1-mini", 310, 40));

        SenseComparisonResponse response = service.compare(user, request(BIG, LARGE, true));

        assertThat(response).isEqualTo(new SenseComparisonResponse("Big is the everyday word.", false));
        ArgumentCaptor<String> system = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<OpenAiChatClient.Turn>> turns = ArgumentCaptor.captor();
        verify(model).complete(system.capture(), turns.capture());
        assertThat(system.getValue()).contains("Write in Ukrainian").contains("Use only the facts given");
        assertThat(turns.getValue())
                .singleElement()
                .satisfies(turn -> assertThat(turn.content())
                        .contains(
                                "\"word\":\"big\"",
                                "large in size",
                                "\"word\":\"large\"",
                                "They live in a large house.")
                        .doesNotContain("regions", "null"));
        ArgumentCaptor<NewComparison> saved = ArgumentCaptor.forClass(NewComparison.class);
        verify(store).insert(saved.capture(), eq(NOW));
        assertThat(saved.getValue())
                .extracting(
                        NewComparison::firstSenseKey,
                        NewComparison::secondSenseKey,
                        NewComparison::explanationLanguage,
                        NewComparison::model,
                        NewComparison::promptTokens,
                        NewComparison::completionTokens,
                        NewComparison::requestedBy)
                .containsExactly(BIG, LARGE, "uk", "gpt-4.1-mini", 310, 40, user.getId());
    }

    @Test
    void findsTheSameExplanationWhicheverWayRoundThePairIsAskedAndNeverPaysForItTwice() {
        known();
        ArgumentCaptor<String> hashes = ArgumentCaptor.forClass(String.class);
        when(store.summary(hashes.capture())).thenReturn(Optional.of("Bought before."));

        assertThat(service.compare(user, request(BIG, LARGE, true)).summary()).isEqualTo("Bought before.");
        assertThat(service.compare(user, request(LARGE, BIG, true)).summary()).isEqualTo("Bought before.");

        assertThat(hashes.getAllValues()).hasSize(2);
        assertThat(hashes.getAllValues().get(0)).isEqualTo(hashes.getAllValues().get(1));
        verify(model, never()).complete(anyString(), any());
        verify(store, never()).insert(any(), any());
    }

    @Test
    void anExplanationIsADifferentPurchaseInAnotherLanguageOrAfterTheEntryWasRewritten() {
        known();
        ArgumentCaptor<String> hashes = ArgumentCaptor.forClass(String.class);
        when(store.summary(hashes.capture())).thenReturn(Optional.of("x"));

        service.compare(user, request(BIG, LARGE, false));
        service.compare(user, new SenseComparisonRequest(BIG, LARGE, Language.DE, false));
        when(store.facts(LARGE)).thenReturn(Optional.of(facts(LARGE, "large", "of great size", List.of())));
        service.compare(user, request(BIG, LARGE, false));

        assertThat(hashes.getAllValues()).doesNotHaveDuplicates().hasSize(3);
    }

    @Test
    void onlyLooksWhenNotAskedToGenerate() {
        known();
        when(store.summary(anyString())).thenReturn(Optional.empty());

        assertThat(service.compare(user, request(BIG, LARGE, false)))
                .isEqualTo(new SenseComparisonResponse(null, false));

        verify(model, never()).complete(anyString(), any());
    }

    @Test
    void stopsBuyingAtTheDailyCeilingWithoutCallingTheModel() {
        known();
        when(store.summary(anyString())).thenReturn(Optional.empty());
        when(store.boughtSince(user.getId(), NOW.minusSeconds(86_400))).thenReturn(30L);

        assertThat(service.compare(user, request(BIG, LARGE, true))).isEqualTo(new SenseComparisonResponse(null, true));

        verify(model, never()).complete(anyString(), any());
        verify(store, never()).insert(any(), any());
    }

    @Test
    void recordsNothingWhenTheModelAnswersWithoutAnExplanation() {
        known();
        when(store.summary(anyString())).thenReturn(Optional.empty());
        when(model.complete(anyString(), any()))
                .thenReturn(new OpenAiChatClient.Completion("{\"summary\": \"  \"}", "gpt-4.1-mini", 300, 2));

        assertThatThrownBy(() -> service.compare(user, request(BIG, LARGE, true)))
                .isInstanceOf(ApiIntegrationException.class);

        verify(store, never()).insert(any(), any());
    }

    @Test
    void refusesASenseTheLexiconDoesNotHoldAndASenseAgainstItself() {
        when(store.facts(BIG)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.compare(user, request(BIG, LARGE, true)))
                .isInstanceOf(EntityNotFoundException.class);
        assertThatThrownBy(() -> service.compare(user, request(BIG, BIG, true)))
                .isInstanceOf(BadUserRequestActionException.class);
        verify(model, never()).complete(anyString(), any());
    }

    private void known() {
        when(store.facts(BIG)).thenReturn(Optional.of(facts(BIG, "big", "large in size", List.of())));
        when(store.facts(LARGE))
                .thenReturn(Optional.of(facts(LARGE, "large", "big in size", List.of("They live in a large house."))));
    }

    private static SenseComparisonRequest request(UUID first, UUID second, boolean generate) {
        return new SenseComparisonRequest(first, second, Language.UK, generate);
    }

    private static SenseFacts facts(UUID key, String headword, String definition, List<String> examples) {
        return new SenseFacts(
                key,
                "en",
                headword,
                "ADJ",
                null,
                definition,
                "A1",
                null,
                null,
                null,
                null,
                null,
                "[{\"lemma\": \"small\"}]",
                null,
                examples);
    }
}
