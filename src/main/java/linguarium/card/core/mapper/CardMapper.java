package linguarium.card.core.mapper;

import linguarium.card.core.dto.CardCreationDto;
import linguarium.card.core.dto.CardDto;
import linguarium.card.core.dto.CardUpdateDto;
import linguarium.card.core.model.entity.Card;
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
