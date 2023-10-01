package com.linguarium.user.service.impl;

import com.linguarium.card.model.CardTag;
import com.linguarium.card.model.Tag;
import com.linguarium.card.repository.CardTagRepository;
import com.linguarium.card.repository.TagRepository;
import com.linguarium.user.dto.LangCodeDto;
import com.linguarium.user.model.Learner;
import com.linguarium.user.repository.LearnerRepository;
import com.linguarium.user.service.LearnerService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LearnerServiceImpl implements LearnerService {
    LearnerRepository learnerRepository;
    CardTagRepository cardTagRepository;
    TagRepository tagRepository;

    @Override
    @Transactional
    public void renameTagForUser(Learner user, Tag tag, String proposedName) {
        if (tag.getText().equals(Tag.normalizeText(proposedName))) {
            return;
        }
        Set<CardTag> foundTaggedCards = cardTagRepository.getByLearnerAndTag(user, tag);

        if (foundTaggedCards.isEmpty()) {
            return;
        }

        Optional<Tag> tagOptional = tagRepository.findByText(proposedName);
        Tag proposedTag;
        if (tagOptional.isPresent()) {
            proposedTag = tagOptional.get();
        } else {
            proposedTag = new Tag(proposedName);
            tagRepository.save(proposedTag);
        }
        foundTaggedCards.forEach(cardTag -> {
            cardTag.setTag(proposedTag);
            cardTagRepository.save(cardTag);
        });
    }

    @Override
    @Transactional
    public void setTargetLangs(LangCodeDto dto, Learner learner) {
        Set<String> languages = new HashSet<>(Arrays.asList(dto.getCodes()));
        learner.setTargetLangs(languages);
        learnerRepository.save(learner);
    }

    @Override
    @Transactional
    public void setFluentLangs(LangCodeDto dto, Learner learner) {
        Set<String> languages = new HashSet<>(Arrays.asList(dto.getCodes()));
        learner.setFluentLangs(languages);
        learnerRepository.save(learner);
    }
}
