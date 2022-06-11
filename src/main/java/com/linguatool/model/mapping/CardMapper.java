package com.linguatool.model.mapping;

import com.linguatool.model.dto.api.request.CardCreationDto;
import com.linguatool.model.dto.api.request.ExampleDto;
import com.linguatool.model.dto.api.request.TranslationDto;
import com.linguatool.model.entity.lang.Card;
import com.linguatool.model.entity.lang.Example;
import com.linguatool.model.entity.lang.Translation;
import org.mapstruct.Mapper;

@Mapper
public interface CardMapper {
    Card cardDtoToEntity(CardCreationDto cardCreationDto);

    Translation translationDtoToEntity(TranslationDto dto);

    Example exampleDtoToEntity(ExampleDto dto);

}
