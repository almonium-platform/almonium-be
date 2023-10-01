package com.linguarium.card.mapper;

import com.linguarium.card.dto.CardCreationDto;
import com.linguarium.card.dto.CardDto;
import com.linguarium.card.dto.CardUpdateDto;
import com.linguarium.card.model.Card;
import org.mapstruct.*;

@Mapper
public interface CardMapper {

    Card cardDtoToEntity(CardCreationDto dto);

    @Mapping(target = "id", expression = "java(null)")
    Card copyCardDtoToEntity(CardDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "translations", ignore = true)
    @Mapping(target = "examples", ignore = true)
    void update(CardUpdateDto dto, @MappingTarget Card card);

    @Mapping(target = "tags", source = "cardTags")
    CardDto cardEntityToDto(Card cardEntity);
}
