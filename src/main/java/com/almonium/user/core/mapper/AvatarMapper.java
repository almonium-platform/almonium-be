package com.almonium.user.core.mapper;

import com.almonium.user.core.dto.response.AvatarDto;
import com.almonium.user.core.model.entity.Avatar;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface AvatarMapper {
    AvatarDto toDto(Avatar avatar);

    List<AvatarDto> toDto(List<Avatar> avatars);
}
