package com.almonium.auth.local.mapper;

import com.almonium.auth.local.dto.response.VerificationTokenDto;
import com.almonium.auth.local.model.entity.VerificationToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VerificationTokenMapper {
    @Mapping(target = "email", source = "principal.email")
    VerificationTokenDto toDto(VerificationToken verificationToken);
}
