package com.linguarium.user.service;

import com.linguarium.user.model.Profile;

public interface ProfileService {
    void updateLoginStreak(Profile user);
}
