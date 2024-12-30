package com.almonium.user.core.mapper;

import com.almonium.user.core.dto.InterestDto;
import com.almonium.user.core.model.entity.Interest;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface InterestMapper {
    InterestDto toDto(Interest avatar);

    List<InterestDto> toDto(List<Interest> avatars);
}
