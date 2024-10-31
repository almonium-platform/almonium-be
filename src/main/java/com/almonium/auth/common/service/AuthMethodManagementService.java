package com.almonium.auth.common.service;

import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import java.util.List;

public interface AuthMethodManagementService {
    void changeEmail(String token);

    void changePassword(long id, String newPassword);

    void requestEmailChange(long id, String newEmail);

    void sendEmailVerification(long id);

    boolean isEmailVerified(long id);

    boolean isEmailAvailable(String email);

    void linkLocal(long userId, String password);

    void linkLocalWithNewEmail(long id, LocalAuthRequest request);

    void unlinkAuthMethod(long userId, AuthProviderType providerType);

    List<AuthProviderType> getAuthProviders(long id);

    void verifyEmail(String token);

    void resetPassword(String token, String newPassword);
}
