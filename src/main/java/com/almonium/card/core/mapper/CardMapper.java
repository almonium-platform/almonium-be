package com.almonium.card.core.mapper;

import com.almonium.card.core.dto.TagDto;
import com.almonium.card.core.dto.request.CardCreationDto;
import com.almonium.card.core.dto.request.CardUpdateDto;
import com.almonium.card.core.dto.response.CardDto;
import com.almonium.card.core.model.entity.CardTag;
import com.almonium.card.core.model.entity.LearningItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface CardMapper {

    LearningItem cardDtoToEntity(CardCreationDto dto);

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "publicId", ignore = true)
    LearningItem copyCardDtoToEntity(CardDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "language", ignore = true)
    @Mapping(target = "examples", ignore = true)
    @Mapping(target = "translations", ignore = true)
    void update(CardUpdateDto dto, @MappingTarget LearningItem card);

    @Mapping(target = "tags", source = "cardTags")
    @Mapping(target = "iteration", source = "totalReviews")
    CardDto cardEntityToDto(LearningItem cardEntity);

    @Mapping(target = "text", source = "tag.text")
    TagDto cardTagToTagDto(CardTag cardTag);
}
