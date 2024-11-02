package com.almonium.auth.common.service;

import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.local.dto.request.LocalAuthRequest;

public interface SensitiveAuthActionService {
    void changePassword(long id, String newPassword);

    void linkLocal(long userId, String password);

    void unlinkAuthMethod(long userId, AuthProviderType providerType);

    void linkLocalWithNewEmail(long id, LocalAuthRequest request);

    // email change actions
    void requestEmailChange(long id, String newEmail);

    void cancelEmailChangeRequest(long id);

    void resendEmailChangeRequest(long id);
}
