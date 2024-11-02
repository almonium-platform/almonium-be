package com.almonium.auth.local.service;

public interface LocalAuthVerificationService {
    void changeEmail(String token);

    void verifyEmail(String token);

    void resetPassword(String token, String newPassword);
}
