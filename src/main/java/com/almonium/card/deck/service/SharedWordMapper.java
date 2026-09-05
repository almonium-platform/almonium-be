package com.almonium.card.deck.service;

import com.almonium.card.core.model.entity.Example;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.model.entity.Translation;
import com.almonium.card.deck.dto.response.SharedExampleDto;
import com.almonium.card.deck.dto.response.SharedWordDto;
import org.springframework.stereotype.Component;

/** Strips a learning item down to what a stranger may see. */
@Component
public class SharedWordMapper {

    public SharedWordDto toShared(LearningItem item) {
        return new SharedWordDto(
                item.getId(),
                item.getEntry(),
                item.getPartOfSpeech(),
                item.getSelectedSense(),
                item.getTranslations().stream().map(Translation::getTranslation).toList(),
                item.getExamples().stream()
                        .map(example -> new SharedExampleDto(example.getExample(), example.getTranslation()))
                        .toList(),
                item.getSourceContext());
    }

    /** A fresh copy for another learner: the word and its senses, none of the owner's progress. */
    public LearningItem copyFor(com.almonium.user.core.model.entity.Learner learner, LearningItem source) {
        LearningItem copy = LearningItem.builder()
                .entry(source.getEntry())
                .normalizedForm(source.getNormalizedForm())
                .lemma(source.getLemma())
                .itemType(source.getItemType())
                .partOfSpeech(source.getPartOfSpeech())
                .selectedSense(source.getSelectedSense())
                .sourceContext(source.getSourceContext())
                .language(source.getLanguage())
                .frequency(source.getFrequency())
                .build();
        copy.setOwner(learner);
        copy.setLegacyOwnerId(learner.getUser().getId());
        source.getTranslations()
                .forEach(translation -> copy.getTranslations()
                        .add(Translation.builder()
                                .translation(translation.getTranslation())
                                .card(copy)
                                .build()));
        source.getExamples()
                .forEach(example -> copy.getExamples()
                        .add(Example.builder()
                                .example(example.getExample())
                                .translation(example.getTranslation())
                                .card(copy)
                                .build()));
        return copy;
    }
}
