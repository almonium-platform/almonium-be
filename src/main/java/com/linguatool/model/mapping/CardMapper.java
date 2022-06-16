package com.linguatool.model.mapping;

import com.linguatool.model.dto.api.request.CardCreationDto;
import com.linguatool.model.dto.api.request.CardDto;
import com.linguatool.model.dto.api.request.ExampleDto;
import com.linguatool.model.dto.api.request.LanguageDto;
import com.linguatool.model.dto.api.request.TagDto;
import com.linguatool.model.dto.api.request.TranslationDto;
import com.linguatool.model.entity.lang.Card;
import com.linguatool.model.entity.lang.Example;
import com.linguatool.model.entity.lang.LanguageEntity;
import com.linguatool.model.entity.lang.Translation;
import com.linguatool.model.entity.user.Language;
import com.linguatool.model.entity.user.Tag;
import com.linguatool.repository.LanguageRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper
public interface CardMapper {

    @Mapping(target = "language", expression = "java(languageMapping(dto.getLanguage(),repo))")
    Card cardDtoToEntity(CardCreationDto dto, LanguageRepository repo);

    CardDto cardEntityToDto(Card cardEntity);

    Translation translationDtoToEntity(TranslationDto dto);

    Tag tagDtoToEntity(TagDto dto);

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

    Example exampleDtoToEntity(ExampleDto dto);

}
