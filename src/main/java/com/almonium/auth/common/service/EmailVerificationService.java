package com.almonium.auth.common.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.exception.BadAuthActionRequest;
import com.almonium.auth.local.dto.response.VerificationTokenDto;
import com.almonium.auth.local.mapper.VerificationTokenMapper;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmailVerificationService {
    UserService userService;
    VerificationTokenManagementService tokenService;
    VerificationTokenMapper verificationTokenMapper;

    @Transactional // TODO why it's needed?
    public void sendEmailVerification(long id) {
        User user = userService.getById(id);
        LocalPrincipal localPrincipal = userService
                .getLocalPrincipal(user)
                .orElseThrow(() -> new BadAuthActionRequest(
                        "Email verification is not available without local authentication method"));

        tokenService.createAndSendVerificationToken(localPrincipal, TokenType.EMAIL_VERIFICATION);
    }

    public Optional<VerificationTokenDto> getLastEmailVerificationToken(long id) {
        return tokenService.findValidEmailVerificationToken(id).map(verificationTokenMapper::toDto);
    }
}
