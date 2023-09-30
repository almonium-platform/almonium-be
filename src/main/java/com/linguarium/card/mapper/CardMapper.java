package com.linguarium.card.mapper;

import com.linguarium.card.dto.*;
import com.linguarium.card.model.Card;
import com.linguarium.card.model.CardTag;
import com.linguarium.card.model.Example;
import com.linguarium.card.model.Translation;
import com.linguarium.translator.model.Language;
import com.linguarium.translator.model.LanguageEntity;
import com.linguarium.translator.repository.LanguageRepository;
import org.mapstruct.*;

import java.util.Optional;

@Mapper
public interface CardMapper {

    @Mapping(target = "language", expression = "java(languageMapping(dto.getLanguage(),repo))")
    Card cardDtoToEntity(CardCreationDto dto, LanguageRepository repo);

    @Mapping(target = "language", expression = "java(languageMapping(dto.getLanguage(),repo))")
    @Mapping(target = "id", expression = "java(null)")
    Card copyCardDtoToEntity(CardDto dto, LanguageRepository repo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "language", expression = "java(updateLanguageMapping(dto.getLanguage(),card,repo))")
    @Mapping(target = "translations", ignore = true)
    @Mapping(target = "examples", ignore = true)
    void update(CardUpdateDto dto, @MappingTarget Card card, LanguageRepository repo);

    @Mapping(target = "tags", source = "cardTags")
    CardDto cardEntityToDto(Card cardEntity);

    Translation translationDtoToEntity(TranslationDto dto);

    default TagDto tagEntityToDto(CardTag entity) {
        return TagDto.builder()
                .text((entity.getTag()).getText())
                .build();
    }

    default LanguageEntity languageMapping(LanguageDto dto, LanguageRepository repo) {
        Optional<LanguageEntity> languageEntityOptional = repo.findByCode(
                Language.fromString(dto.getLanguageCode())
        );
        if (languageEntityOptional.isPresent()) {
            return languageEntityOptional.get();
        } else {
            throw new IllegalArgumentException("");
        }
    }

    //used in update
    default LanguageEntity updateLanguageMapping(LanguageDto dto, Card card, LanguageRepository repo) {
        if (dto == null && card != null) {
            return card.getLanguage();
        } else {
            return languageMapping(dto, repo);
        }
    }

    default LanguageDto languageEntityToDto(LanguageEntity entity) {
        return LanguageDto.builder()
                .languageCode(entity.getCode().getCode())
                .build();
    }

    Example exampleDtoToEntity(ExampleDto dto);
}
