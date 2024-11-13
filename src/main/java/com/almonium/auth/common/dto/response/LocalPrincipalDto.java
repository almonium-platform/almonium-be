package com.almonium.auth.common.dto.response;

import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LocalPrincipalDto extends PrincipalDto {
    LocalDate lastPasswordResetDate;
}
