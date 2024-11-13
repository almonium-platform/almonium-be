package com.almonium.auth.common.service;

import com.almonium.auth.common.dto.response.PrincipalDto;
import java.util.List;

public interface AuthMethodManagementService {
    boolean isEmailAvailable(String email);

    List<PrincipalDto> getAuthProviders(String email);
}
