package linguarium.user.core.service;

import linguarium.user.core.model.Profile;

public interface ProfileService {
    void updateLoginStreak(Profile user);
}
