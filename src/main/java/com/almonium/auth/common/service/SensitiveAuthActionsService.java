package com.almonium.auth.common.service;

import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.user.core.model.entity.User;

public interface SensitiveAuthActionsService {
    void changePassword(long id, String newPassword);

    void linkLocal(long userId, String password);

    void unlinkAuthMethod(long userId, AuthProviderType providerType);

    void linkLocalWithNewEmail(long id, LocalAuthRequest request);

    void deleteAccount(User user);

    // email change actions
    void requestEmailChange(long id, String newEmail);

    void cancelEmailChangeRequest(long id);

    void resendEmailChangeRequest(long id);
}
