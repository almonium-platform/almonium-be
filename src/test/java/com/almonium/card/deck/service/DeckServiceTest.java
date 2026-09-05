package com.almonium.card.deck.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.repository.LearningItemRepository;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.card.deck.dto.request.DeckCreationRequest;
import com.almonium.card.deck.dto.request.DeckUpdateRequest;
import com.almonium.card.deck.dto.response.DeckDto;
import com.almonium.card.deck.model.entity.Deck;
import com.almonium.card.deck.model.entity.DeckItem;
import com.almonium.card.deck.repository.DeckItemRepository;
import com.almonium.card.deck.repository.DeckRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class DeckServiceTest {

    @Mock
    DeckRepository deckRepository;

    @Mock
    DeckItemRepository deckItemRepository;

    @Mock
    LearningItemRepository learningItemRepository;

    @Mock
    LearnerFinder learnerFinder;

    @Mock
    ShareIdGenerator shareIdGenerator;

    @InjectMocks
    DeckService service;

    User owner;
    Learner learner;

    @BeforeEach
    void setUp() {
        owner = User.builder().id(UUID.randomUUID()).build();
        learner = Learner.builder()
                .id(UUID.randomUUID())
                .user(owner)
                .language(Language.DE)
                .build();
    }

    @Test
    @DisplayName("A new deck gets a share id at birth, with the link off until the owner turns it on")
    void newDeckHasShareIdAndLinkOff() {
        when(learnerFinder.findLearner(owner, Language.DE)).thenReturn(learner);
        when(shareIdGenerator.next()).thenReturn("taken000", "fresh000");
        when(deckRepository.existsByShareId("taken000")).thenReturn(true);
        when(deckRepository.existsByShareId("fresh000")).thenReturn(false);

        DeckDto dto = service.createDeck(owner, new DeckCreationRequest("  Winter reading ", Language.DE, null));

        assertThat(dto.title()).isEqualTo("Winter reading");
        assertThat(dto.shareId()).isEqualTo("fresh000");
        assertThat(dto.shareEnabled()).isFalse();
        assertThat(dto.wordIds()).isEmpty();
        verify(deckItemRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Setting the words keeps the owner's order and refuses words of another language")
    void wordsKeepOrderAndOneLanguage() {
        Deck deck = Deck.builder()
                .id(UUID.randomUUID())
                .owner(learner)
                .language(Language.DE)
                .build();
        when(deckRepository.findByIdAndOwnerUserIdAndDeletedAtIsNull(deck.getId(), owner.getId()))
                .thenReturn(Optional.of(deck));
        LearningItem first = LearningItem.builder()
                .id(UUID.randomUUID())
                .language(Language.DE)
                .build();
        LearningItem second = LearningItem.builder()
                .id(UUID.randomUUID())
                .language(Language.DE)
                .build();
        when(learningItemRepository.findByIdAndOwnerUserId(first.getId(), owner.getId()))
                .thenReturn(Optional.of(first));
        when(learningItemRepository.findByIdAndOwnerUserId(second.getId(), owner.getId()))
                .thenReturn(Optional.of(second));

        DeckDto dto = service.setWords(owner, deck.getId(), List.of(second.getId(), first.getId(), second.getId()));

        assertThat(dto.wordIds()).containsExactly(second.getId(), first.getId());
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<DeckItem>> saved = ArgumentCaptor.forClass(List.class);
        verify(deckItemRepository).saveAll(saved.capture());
        assertThat(saved.getValue()).extracting(DeckItem::getPosition).containsExactly(0, 1);

        LearningItem spanish = LearningItem.builder()
                .id(UUID.randomUUID())
                .language(Language.ES)
                .build();
        when(learningItemRepository.findByIdAndOwnerUserId(spanish.getId(), owner.getId()))
                .thenReturn(Optional.of(spanish));
        assertThatThrownBy(() -> service.setWords(owner, deck.getId(), List.of(spanish.getId())))
                .isInstanceOf(BadUserRequestActionException.class);
    }

    @Test
    @DisplayName("Someone else's card cannot be put in a deck")
    void foreignCardsAreRejected() {
        Deck deck = Deck.builder()
                .id(UUID.randomUUID())
                .owner(learner)
                .language(Language.DE)
                .build();
        when(deckRepository.findByIdAndOwnerUserIdAndDeletedAtIsNull(deck.getId(), owner.getId()))
                .thenReturn(Optional.of(deck));
        UUID foreign = UUID.randomUUID();
        when(learningItemRepository.findByIdAndOwnerUserId(foreign, owner.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.setWords(owner, deck.getId(), List.of(foreign)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("The switch flips the link without touching the title, and deleting is soft")
    void switchAndSoftDelete() {
        Deck deck = Deck.builder()
                .id(UUID.randomUUID())
                .owner(learner)
                .language(Language.DE)
                .title("Winter reading")
                .shareId("8fk2qaZZ")
                .build();
        when(deckRepository.findByIdAndOwnerUserIdAndDeletedAtIsNull(deck.getId(), owner.getId()))
                .thenReturn(Optional.of(deck));

        DeckDto on = service.updateDeck(owner, deck.getId(), new DeckUpdateRequest(null, true));
        assertThat(on.shareEnabled()).isTrue();
        assertThat(on.title()).isEqualTo("Winter reading");

        service.deleteDeck(owner, deck.getId());
        assertThat(deck.isDeleted()).isTrue();
        assertThat(deck.isShareEnabled()).isFalse();
        verify(deckRepository, never()).delete(any(Deck.class));
    }
}
