package com.linguarium.card.core.mapper;

import com.linguarium.card.core.dto.CardCreationDto;
import com.linguarium.card.core.dto.CardDto;
import com.linguarium.card.core.dto.CardUpdateDto;
import com.linguarium.card.core.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface CardMapper {

    Card cardDtoToEntity(CardCreationDto dto);

    @Mapping(target = "id", expression = "java(null)")
    Card copyCardDtoToEntity(CardDto dto);

    void update(CardUpdateDto dto, @MappingTarget Card card);

    @Mapping(target = "tags", source = "cardTags")
    CardDto cardEntityToDto(Card cardEntity);
}
