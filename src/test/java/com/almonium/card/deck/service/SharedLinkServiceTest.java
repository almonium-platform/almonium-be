package com.almonium.card.deck.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.model.entity.Translation;
import com.almonium.card.core.repository.ExampleRepository;
import com.almonium.card.core.repository.LearningItemRepository;
import com.almonium.card.core.repository.TranslationRepository;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.card.deck.dto.response.AddedWordsResult;
import com.almonium.card.deck.dto.response.SharedDeckView;
import com.almonium.card.deck.dto.response.SharedLinkViewerStatus;
import com.almonium.card.deck.model.entity.Deck;
import com.almonium.card.deck.model.entity.DeckItem;
import com.almonium.card.deck.model.enums.SharedLinkStatus;
import com.almonium.card.deck.repository.DeckRepository;
import com.almonium.subscription.service.EffectiveAccessService;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class SharedLinkServiceTest {

    @Mock
    DeckRepository deckRepository;

    @Mock
    LearningItemRepository learningItemRepository;

    @Mock
    TranslationRepository translationRepository;

    @Mock
    ExampleRepository exampleRepository;

    @Mock
    LearnerRepository learnerRepository;

    @Mock
    LearnerFinder learnerFinder;

    @Mock
    EffectiveAccessService effectiveAccessService;

    @Spy
    SharedWordMapper sharedWordMapper = new SharedWordMapper();

    @InjectMocks
    SharedLinkService service;

    User owner;
    Learner ownerLearner;
    User viewer;
    Learner viewerLearner;
    Deck deck;
    LearningItem verschweigen;
    LearningItem urteil;

    @BeforeEach
    void setUp() {
        owner = User.builder().id(UUID.randomUUID()).username("kuzanoleg").build();
        owner.setProfile(Profile.builder().avatarUrl("https://cdn/avatar.png").build());
        ownerLearner = Learner.builder()
                .id(UUID.randomUUID())
                .user(owner)
                .language(Language.DE)
                .build();
        viewer = User.builder().id(UUID.randomUUID()).username("familsubs").build();
        viewerLearner = Learner.builder()
                .id(UUID.randomUUID())
                .user(viewer)
                .language(Language.DE)
                .build();

        verschweigen = word(ownerLearner, "verschweigen", "to keep something secret");
        urteil = word(ownerLearner, "das Urteil", "a verdict");
        deck = Deck.builder()
                .id(UUID.randomUUID())
                .owner(ownerLearner)
                .language(Language.DE)
                .title("Der Vorleser — chapters 1 to 4")
                .shareId("8fk2qaZZ")
                .shareEnabled(true)
                .build();
        deck.getItems()
                .add(DeckItem.builder()
                        .deck(deck)
                        .item(verschweigen)
                        .position(0)
                        .build());
        deck.getItems()
                .add(DeckItem.builder().deck(deck).item(urteil).position(1).build());
    }

    @Test
    @DisplayName("An active link shows the deck, its words and who shared it")
    void activeDeckIsShownInFull() {
        when(deckRepository.findByShareId("8fk2qaZZ")).thenReturn(Optional.of(deck));
        when(effectiveAccessService.isPremium(owner)).thenReturn(true);

        SharedDeckView view = service.viewDeck("8fk2qaZZ");

        assertThat(view.status()).isEqualTo(SharedLinkStatus.ACTIVE);
        assertThat(view.title()).isEqualTo("Der Vorleser — chapters 1 to 4");
        assertThat(view.language()).isEqualTo(Language.DE);
        assertThat(view.words()).extracting("entry").containsExactly("verschweigen", "das Urteil");
        assertThat(view.words().get(0).translations()).containsExactly("to keep something secret");
        assertThat(view.sharer().username()).isEqualTo("kuzanoleg");
        assertThat(view.sharer().avatarUrl()).isEqualTo("https://cdn/avatar.png");
        assertThat(view.sharer().premium()).isTrue();
    }

    @Test
    @DisplayName("A revoked link answers with its status and never names the owner")
    void revokedLinkNamesNobody() {
        deck.setShareEnabled(false);
        when(deckRepository.findByShareId("8fk2qaZZ")).thenReturn(Optional.of(deck));

        SharedDeckView view = service.viewDeck("8fk2qaZZ");

        assertThat(view.status()).isEqualTo(SharedLinkStatus.REVOKED);
        assertThat(view.title()).isNull();
        assertThat(view.words()).isEmpty();
        assertThat(view.sharer()).isNull();
        verify(effectiveAccessService, never()).isPremium(any());
    }

    @Test
    @DisplayName("A deleted deck is told apart from a revoked one")
    void deletedDeckIsDeleted() {
        deck.setDeletedAt(Instant.now());
        deck.setShareEnabled(false);
        when(deckRepository.findByShareId("8fk2qaZZ")).thenReturn(Optional.of(deck));

        assertThat(service.viewDeck("8fk2qaZZ").status()).isEqualTo(SharedLinkStatus.DELETED);
    }

    @Test
    @DisplayName("An id nobody ever issued is not found")
    void unknownIdIsNotFound() {
        when(deckRepository.findByShareId("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.viewDeck("nope")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("The viewer status marks words already held, counts those due, and knows the owner")
    void viewerStatusReportsHeldWordsAndOwner() {
        when(deckRepository.findByShareId("8fk2qaZZ")).thenReturn(Optional.of(deck));
        when(learnerRepository.findByUserIdAndLanguage(viewer.getId(), Language.DE))
                .thenReturn(Optional.of(viewerLearner));
        LearningItem mine = word(viewerLearner, "das Urteil", "a verdict");
        mine.setDueAt(Instant.now().minus(1, ChronoUnit.HOURS));
        when(learningItemRepository.findAllByOwnerAndNormalizedFormIn(eq(viewerLearner), anyCollection()))
                .thenReturn(List.of(mine));

        SharedLinkViewerStatus status = service.deckViewer(viewer, "8fk2qaZZ");

        assertThat(status.owner()).isFalse();
        assertThat(status.hasLearner()).isTrue();
        assertThat(status.heldWordIds()).containsExactly(urteil.getId());
        assertThat(status.dueAmongHeld()).isEqualTo(1);

        when(learnerRepository.findByUserIdAndLanguage(owner.getId(), Language.DE))
                .thenReturn(Optional.of(ownerLearner));
        when(learningItemRepository.findAllByOwnerAndNormalizedFormIn(eq(ownerLearner), anyCollection()))
                .thenReturn(List.of(verschweigen, urteil));
        assertThat(service.deckViewer(owner, "8fk2qaZZ").owner()).isTrue();
    }

    @Test
    @DisplayName("A viewer without the deck's language holds nothing and is told so")
    void viewerWithoutLanguageHoldsNothing() {
        when(deckRepository.findByShareId("8fk2qaZZ")).thenReturn(Optional.of(deck));
        when(learnerRepository.findByUserIdAndLanguage(viewer.getId(), Language.DE))
                .thenReturn(Optional.empty());

        SharedLinkViewerStatus status = service.deckViewer(viewer, "8fk2qaZZ");

        assertThat(status.hasLearner()).isFalse();
        assertThat(status.heldWordIds()).isEmpty();
    }

    @Test
    @DisplayName("Adding copies the chosen words, skips what the viewer already holds, and is idempotent")
    void addingCopiesAndSkipsHeld() {
        when(deckRepository.findByShareId("8fk2qaZZ")).thenReturn(Optional.of(deck));
        when(learnerRepository.findByUserIdAndLanguage(viewer.getId(), Language.DE))
                .thenReturn(Optional.of(viewerLearner));
        when(learningItemRepository.findAllByOwnerAndNormalizedFormIn(eq(viewerLearner), anyCollection()))
                .thenReturn(List.of(word(viewerLearner, "das Urteil", "a verdict")));

        AddedWordsResult result =
                service.addFromDeck(viewer, "8fk2qaZZ", List.of(verschweigen.getId(), urteil.getId()));

        assertThat(result.added()).isEqualTo(1);
        assertThat(result.alreadyHeld()).isEqualTo(1);
        assertThat(result.firstDueAt()).isNotNull();
        ArgumentCaptor<LearningItem> saved = ArgumentCaptor.forClass(LearningItem.class);
        verify(learningItemRepository).save(saved.capture());
        LearningItem copy = saved.getValue();
        assertThat(copy.getEntry()).isEqualTo("verschweigen");
        assertThat(copy.getOwner()).isEqualTo(viewerLearner);
        assertThat(copy.getLegacyOwnerId()).isEqualTo(viewer.getId());
        assertThat(copy.getId()).isNull();
        assertThat(copy.getTranslations()).extracting("translation").containsExactly("to keep something secret");
        assertThat(copy.getTranslations().get(0).getCard()).isSameAs(copy);
        assertThat(copy.getTotalReviews()).isZero();
    }

    @Test
    @DisplayName("Words not in the deck are ignored, so a stranger cannot copy an arbitrary card by id")
    void addingIgnoresWordsOutsideTheDeck() {
        when(deckRepository.findByShareId("8fk2qaZZ")).thenReturn(Optional.of(deck));
        when(learnerRepository.findByUserIdAndLanguage(viewer.getId(), Language.DE))
                .thenReturn(Optional.of(viewerLearner));

        AddedWordsResult result = service.addFromDeck(viewer, "8fk2qaZZ", List.of(UUID.randomUUID()));

        assertThat(result.added()).isZero();
        verify(learningItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Nothing can be added from a revoked link")
    void revokedLinkRefusesAdds() {
        deck.setShareEnabled(false);
        when(deckRepository.findByShareId("8fk2qaZZ")).thenReturn(Optional.of(deck));

        assertThatThrownBy(() -> service.addFromDeck(viewer, "8fk2qaZZ", List.of(verschweigen.getId())))
                .isInstanceOf(BadUserRequestActionException.class);
    }

    @Test
    @DisplayName("Adding needs the deck's language on the viewer's account")
    void addingNeedsTheLanguage() {
        when(deckRepository.findByShareId("8fk2qaZZ")).thenReturn(Optional.of(deck));
        when(learnerRepository.findByUserIdAndLanguage(viewer.getId(), Language.DE))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addFromDeck(viewer, "8fk2qaZZ", List.of(verschweigen.getId())))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("DE");
    }

    @Test
    @DisplayName("A single card is shared by its public id and copied the same way")
    void cardIsSharedByPublicId() {
        UUID publicId = verschweigen.getPublicId();
        when(learningItemRepository.getByPublicId(publicId)).thenReturn(Optional.of(verschweigen));
        when(effectiveAccessService.isPremium(owner)).thenReturn(false);

        var view = service.viewCard(publicId);
        assertThat(view.language()).isEqualTo(Language.DE);
        assertThat(view.word().entry()).isEqualTo("verschweigen");
        assertThat(view.sharer().premium()).isFalse();

        when(learnerRepository.findByUserIdAndLanguage(viewer.getId(), Language.DE))
                .thenReturn(Optional.of(viewerLearner));
        when(learningItemRepository.findAllByOwnerAndNormalizedFormIn(eq(viewerLearner), anyCollection()))
                .thenReturn(List.of());
        assertThat(service.addCard(viewer, publicId).added()).isEqualTo(1);
    }

    private static LearningItem word(Learner learner, String entry, String translation) {
        LearningItem item = LearningItem.builder()
                .id(UUID.randomUUID())
                .entry(entry)
                .normalizedForm(entry.trim().toLowerCase())
                .language(Language.DE)
                .owner(learner)
                .translations(new ArrayList<>())
                .examples(new ArrayList<>())
                .build();
        item.getTranslations()
                .add(Translation.builder().translation(translation).card(item).build());
        return item;
    }
}
