package com.almonium.auth.common.mapper;

import com.almonium.auth.common.dto.response.PrincipalDto;
import com.almonium.auth.common.model.entity.Principal;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface PrincipalMapper {
    PrincipalDto toDto(Principal principal);

    List<PrincipalDto> toDto(List<Principal> principals);
}
