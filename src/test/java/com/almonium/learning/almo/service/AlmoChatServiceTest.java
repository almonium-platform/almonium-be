package com.almonium.learning.almo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.repository.LearningItemRepository;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.config.properties.AlmoProperties;
import com.almonium.learning.almo.client.OpenAiChatClient;
import com.almonium.learning.almo.dto.AlmoChatDto;
import com.almonium.learning.almo.dto.AlmoOpenerDto;
import com.almonium.learning.almo.dto.AlmoReplyDto;
import com.almonium.learning.almo.model.AlmoTurn;
import com.almonium.learning.almo.repository.AlmoQueueRepository;
import com.almonium.learning.almo.repository.AlmoTurnRepository;
import com.almonium.learning.review.repository.ConfusionEdgeRepository;
import com.almonium.learning.review.service.ReviewService;
import com.almonium.subscription.service.EffectiveAccessService;
import com.almonium.user.core.exception.ResourceNotAccessibleException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import io.getstream.chat.java.models.Message;
import java.time.Instant;
import java.util.List;
import java.util.Map;
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
class AlmoChatServiceTest {
    private static final String ALMO = "almo";
    private static final String USER_MESSAGE = "msg-user";

    @Mock
    EffectiveAccessService effectiveAccessService;

    @Mock
    LearnerRepository learnerRepository;

    @Mock
    LearnerFinder learnerFinder;

    @Mock
    LearningItemRepository learningItemRepository;

    @Mock
    AlmoQueueRepository queueRepository;

    @Mock
    ConfusionEdgeRepository confusionEdgeRepository;

    @Mock
    AlmoTurnRepository turnRepository;

    @Mock
    AlmoStreamGateway stream;

    @Mock
    OpenAiChatClient model;

    @Mock
    ReviewService reviewService;

    private final AlmoProperties properties = new AlmoProperties();
    private final AlmoLanguageCopy copy = new AlmoLanguageCopy();

    private AlmoChatService service;
    private User user;
    private Learner learner;
    private LearningItem vornehmen;
    private LearningItem besitzen;
    private LearningItem beherrschen;

    @BeforeEach
    void setUp() {
        service = new AlmoChatService(
                effectiveAccessService,
                learnerRepository,
                learnerFinder,
                learningItemRepository,
                queueRepository,
                confusionEdgeRepository,
                turnRepository,
                stream,
                model,
                new AlmoPromptBuilder(copy),
                copy,
                reviewService,
                properties,
                new ObjectMapper());
        user = User.builder().id(UUID.randomUUID()).build();
        learner = Learner.builder()
                .id(UUID.randomUUID())
                .user(user)
                .language(Language.DE)
                .build();
        vornehmen = item("sich vornehmen");
        besitzen = item("besitzen");
        beherrschen = item("beherrschen");
    }

    @Test
    void freeAccountGetsNoChannelAndStreamIsNeverAsked() {
        when(effectiveAccessService.isPremium(user)).thenReturn(false);

        assertThat(service.ensureChats(user)).isEmpty();

        verify(stream, never()).ensureAlmoUser();
        verify(stream, never()).ensureChannel(any(), any(), any());
    }

    @Test
    void memberGetsOneChannelPerActiveLanguageWithAnOpenerFromTheQueue() {
        when(effectiveAccessService.isPremium(user)).thenReturn(true);
        Learner setAside = Learner.builder().language(Language.FR).active(false).build();
        when(learnerRepository.findAllByUserIdOrderByLanguage(user.getId())).thenReturn(List.of(learner, setAside));
        when(stream.ensureChannel(user.getId(), Language.DE, "Almo · Deutsch")).thenReturn("private:almo_x_de");
        when(learningItemRepository.findAllByOwnerAndLeechFalseAndDueAtLessThanEqualOrderByDueAtAsc(eq(learner), any()))
                .thenReturn(List.of(vornehmen));
        when(queueRepository.findAllByOwnerAndLeechFalseOrderByCreatedAtDesc(eq(learner), any()))
                .thenReturn(List.of(besitzen));

        List<AlmoChatDto> chats = service.ensureChats(user);

        verify(stream).ensureAlmoUser();
        assertThat(chats).hasSize(1);
        AlmoChatDto chat = chats.get(0);
        assertThat(chat.cid()).isEqualTo("private:almo_x_de");
        assertThat(chat.name()).isEqualTo("Almo · Deutsch");
        assertThat(chat.placeholder()).isEqualTo("Schreib auf Deutsch");
        assertThat(chat.openers()).contains(new AlmoOpenerDto("Ein Satz mit sich vornehmen", "sich vornehmen"));
    }

    @Test
    void freeAccountCannotAskForAReply() {
        when(effectiveAccessService.isPremium(user)).thenReturn(false);

        assertThatThrownBy(() -> service.reply(user, Language.DE, USER_MESSAGE))
                .isInstanceOf(ResourceNotAccessibleException.class);
    }

    @Test
    void aMessageAlreadyAnsweredIsNotAnsweredTwice() {
        when(effectiveAccessService.isPremium(user)).thenReturn(true);
        when(model.isConfigured()).thenReturn(true);
        when(turnRepository.findByUserMessageId(USER_MESSAGE))
                .thenReturn(
                        Optional.of(AlmoTurn.builder().replyMessageId("reply-1").build()));

        AlmoReplyDto reply = service.reply(user, Language.DE, USER_MESSAGE);

        assertThat(reply).isEqualTo(new AlmoReplyDto("reply-1", false));
        verify(model, never()).complete(any(), any());
    }

    @Test
    void ceilingReachedAnswersNothingAndSaysSoOnlyToTheClient() {
        when(effectiveAccessService.isPremium(user)).thenReturn(true);
        when(model.isConfigured()).thenReturn(true);
        when(turnRepository.findByUserMessageId(USER_MESSAGE)).thenReturn(Optional.empty());
        when(turnRepository.countByUserIdAndCreatedAtAfter(eq(user.getId()), any()))
                .thenReturn((long) properties.getDailyMessageCeiling());

        AlmoReplyDto reply = service.reply(user, Language.DE, USER_MESSAGE);

        assertThat(reply.ceilingReached()).isTrue();
        assertThat(reply.replyMessageId()).isNull();
        verify(model, never()).complete(any(), any());
        verify(stream, never()).typing(any(), eq(true));
    }

    @Test
    void aTurnSendsTheReplyMarksBothBubblesRecordsEvidenceAndTheCost() {
        openThread(
                message("m1", ALMO, "Du hattest dir vorgenommen, das Kapitel zu beenden. Und?"),
                message(
                        USER_MESSAGE,
                        user.getId().toString(),
                        "Ja, ich habe es geschafft. Michael besitzt die Sprache jetzt."));
        when(model.complete(anyString(), anyList()))
                .thenReturn(new OpenAiChatClient.Completion(
                        """
                        {"reply": "Fast. Besitzen ist für Dinge. Eine Sprache beherrscht man. Was hat dabei geholfen?",
                         "translation": "Almost. Owning is for things. A language is mastered. What helped?",
                         "reply_words": [{"entry": "dabei", "surface": "dabei"}],
                         "learner_words": [{"entry": "sich vornehmen", "surface": "geschafft"}],
                         "contrast": ["Besitzen", "beherrscht"],
                         "confusion": {"misused": "besitzen", "intended": "beherrschen"}}
                        """,
                        "gpt-test",
                        321,
                        45));
        when(stream.sendReply(eq("almo_" + user.getId() + "_de"), anyString(), any()))
                .thenReturn("reply-9");

        AlmoReplyDto reply = service.reply(user, Language.DE, USER_MESSAGE);

        assertThat(reply).isEqualTo(new AlmoReplyDto("reply-9", false));

        ArgumentCaptor<List<OpenAiChatClient.Turn>> history = ArgumentCaptor.forClass(List.class);
        verify(model).complete(anyString(), history.capture());
        assertThat(history.getValue()).extracting(OpenAiChatClient.Turn::role).containsExactly("assistant", "user");

        ArgumentCaptor<Map<String, Object>> fields = ArgumentCaptor.forClass(Map.class);
        verify(stream).sendReply(anyString(), anyString(), fields.capture());
        assertThat(fields.getValue())
                .containsEntry(AlmoChatService.WORDS_FIELD, List.of("dabei"))
                .containsEntry(AlmoChatService.CONTRAST_FIELD, List.of("Besitzen", "beherrscht"))
                .containsEntry(
                        AlmoChatService.TRANSLATION_FIELD,
                        "Almost. Owning is for things. A language is mastered. What helped?");
        verify(stream)
                .annotateMessage(USER_MESSAGE, user.getId(), Map.of(AlmoChatService.WORDS_FIELD, List.of("geschafft")));
        verify(stream).typing("almo_" + user.getId() + "_de", true);
        verify(stream).typing("almo_" + user.getId() + "_de", false);

        verify(reviewService).recordProduction(List.of(vornehmen));
        verify(reviewService).recordMisuse(user, beherrschen, besitzen);

        ArgumentCaptor<AlmoTurn> turn = ArgumentCaptor.forClass(AlmoTurn.class);
        verify(turnRepository).save(turn.capture());
        assertThat(turn.getValue().getUserMessageId()).isEqualTo(USER_MESSAGE);
        assertThat(turn.getValue().getReplyMessageId()).isEqualTo("reply-9");
        assertThat(turn.getValue().getModel()).isEqualTo("gpt-test");
        assertThat(turn.getValue().getPromptTokens()).isEqualTo(321);
        assertThat(turn.getValue().getCompletionTokens()).isEqualTo(45);
        assertThat(turn.getValue().getLanguage()).isEqualTo(Language.DE);
    }

    @Test
    void aModelThatCannotBeReachedStopsTheTypingIndicatorAndBuysNothing() {
        openThread(message(USER_MESSAGE, user.getId().toString(), "Hallo"));
        when(model.complete(anyString(), anyList())).thenThrow(new ApiIntegrationException("down"));

        assertThatThrownBy(() -> service.reply(user, Language.DE, USER_MESSAGE))
                .isInstanceOf(ApiIntegrationException.class);

        verify(stream).typing(anyString(), eq(false));
        verify(stream, never()).sendReply(any(), any(), any());
        verify(turnRepository, never()).save(any());
    }

    @Test
    void aMessageOutsideTheThreadIsRefused() {
        openThread(message("other", user.getId().toString(), "Hallo"));

        assertThatThrownBy(() -> service.reply(user, Language.DE, USER_MESSAGE))
                .isInstanceOf(jakarta.persistence.EntityNotFoundException.class);

        verify(model, never()).complete(any(), any());
    }

    private void openThread(Message... messages) {
        when(effectiveAccessService.isPremium(user)).thenReturn(true);
        when(model.isConfigured()).thenReturn(true);
        when(turnRepository.findByUserMessageId(USER_MESSAGE)).thenReturn(Optional.empty());
        when(turnRepository.countByUserIdAndCreatedAtAfter(eq(user.getId()), any()))
                .thenReturn(0L);
        when(learnerFinder.findLearner(user, Language.DE)).thenReturn(learner);
        when(stream.almoUserId()).thenReturn(ALMO);
        String channelId = "almo_" + user.getId() + "_de";
        when(stream.channelId(user.getId(), Language.DE)).thenReturn(channelId);
        when(stream.recentMessages(eq(channelId), anyInt())).thenReturn(List.of(messages));
        // The thread is read before the queue is, so a refused message never gets this far.
        lenient()
                .when(learningItemRepository.findAllByOwnerAndLeechFalseAndDueAtLessThanEqualOrderByDueAtAsc(
                        eq(learner), any()))
                .thenReturn(List.of(vornehmen, besitzen));
        lenient()
                .when(queueRepository.findAllByOwnerAndLeechFalseOrderByCreatedAtDesc(eq(learner), any()))
                .thenReturn(List.of(beherrschen, vornehmen));
        lenient()
                .when(confusionEdgeRepository.findAllByOwnerAndSourceItemLanguage(user, Language.DE))
                .thenReturn(List.of());
    }

    private static Message message(String id, String authorId, String text) {
        Message message = mock(Message.class);
        io.getstream.chat.java.models.User author = mock(io.getstream.chat.java.models.User.class);
        when(author.getId()).thenReturn(authorId);
        when(message.getId()).thenReturn(id);
        when(message.getUser()).thenReturn(author);
        when(message.getText()).thenReturn(text);
        return message;
    }

    private static LearningItem item(String entry) {
        return LearningItem.builder()
                .id(UUID.randomUUID())
                .entry(entry)
                .language(Language.DE)
                .dueAt(Instant.now())
                .build();
    }
}
