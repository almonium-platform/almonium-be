package com.almonium.auth.common.mapper;

import com.almonium.auth.common.dto.response.LocalPrincipalDto;
import com.almonium.auth.common.dto.response.PrincipalDto;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrincipalMapper {

    PrincipalDto principalToDto(Principal principal);

    @Mapping(target = "lastPasswordResetDate", source = "lastPasswordResetDate")
    LocalPrincipalDto localPrincipalToDto(LocalPrincipal principal);

    default PrincipalDto toDto(Principal principal) {
        if (principal instanceof LocalPrincipal) {
            return localPrincipalToDto((LocalPrincipal) principal);
        } else {
            return principalToDto(principal);
        }
    }

    default List<PrincipalDto> toDto(List<Principal> principals) {
        return principals.stream().map(this::toDto).toList();
    }
}
