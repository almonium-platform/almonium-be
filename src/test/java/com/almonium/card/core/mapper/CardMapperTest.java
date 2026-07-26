package com.almonium.card.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.dto.TagDto;
import com.almonium.card.core.dto.request.CardUpdateDto;
import com.almonium.card.core.dto.response.CardDto;
import com.almonium.card.core.model.entity.Card;
import com.almonium.card.core.model.entity.CardTag;
import com.almonium.card.core.model.entity.Example;
import com.almonium.card.core.model.entity.Tag;
import com.almonium.card.core.model.entity.Translation;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class CardMapperTest {
    private final CardMapper mapper = Mappers.getMapper(CardMapper.class);

    @Test
    void mapsTagTextIntoTheCardContract() {
        CardTag cardTag = CardTag.builder().tag(new Tag("travel")).build();
        Card card = Card.builder().cardTags(new HashSet<>()).build();
        card.getCardTags().add(cardTag);

        CardDto result = mapper.cardEntityToDto(card);

        assertThat(result.getTags()).extracting(TagDto::getText).containsExactly("travel");
    }

    @Test
    void partialUpdateLeavesIdentityAndRelationshipsOwnedByTheService() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.parse("2026-07-26T10:00:00Z");
        ArrayList<Translation> translations = new ArrayList<>(
                java.util.List.of(Translation.builder().translation("hello").build()));
        ArrayList<Example> examples = new ArrayList<>(
                java.util.List.of(Example.builder().example("Bonjour!").build()));
        Card card = Card.builder()
                .id(id)
                .entry("bonjour")
                .language(Language.FR)
                .createdAt(createdAt)
                .translations(translations)
                .examples(examples)
                .build();

        mapper.update(
                CardUpdateDto.builder()
                        .id(UUID.randomUUID())
                        .entry("salut")
                        .language(Language.DE)
                        .build(),
                card);

        assertThat(card.getId()).isEqualTo(id);
        assertThat(card.getEntry()).isEqualTo("salut");
        assertThat(card.getLanguage()).isEqualTo(Language.FR);
        assertThat(card.getCreatedAt()).isEqualTo(createdAt);
        assertThat(card.getTranslations()).isSameAs(translations);
        assertThat(card.getExamples()).isSameAs(examples);
    }
}
