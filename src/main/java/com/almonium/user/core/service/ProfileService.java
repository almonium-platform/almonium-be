package com.almonium.user.core.service;

import com.almonium.user.core.model.entity.Profile;

public interface ProfileService {
    void updateLoginStreak(Profile user);
}
