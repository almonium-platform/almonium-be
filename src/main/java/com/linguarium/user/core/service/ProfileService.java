package com.linguarium.user.core.service;

import com.linguarium.user.core.model.Profile;

public interface ProfileService {
    void updateLoginStreak(Profile user);
}
