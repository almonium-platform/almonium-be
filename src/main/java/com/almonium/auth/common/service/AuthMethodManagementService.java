package com.almonium.auth.common.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.dto.response.PrincipalDto;
import com.almonium.auth.common.mapper.PrincipalMapper;
import com.almonium.auth.common.repository.PrincipalRepository;
import com.almonium.user.core.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthMethodManagementService {
    UserRepository userRepository;
    PrincipalRepository principalRepository;
    PrincipalMapper principalMapper;

    public List<PrincipalDto> getAuthProviders(String email) {
        // searching by email to omit temp unverified principal in case of email migration
        return principalMapper.toDto(principalRepository.findByEmail(email));
    }

    public boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }
}
