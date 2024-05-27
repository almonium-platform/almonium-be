package linguarium.user.core.service;

import linguarium.user.core.model.entity.Profile;

public interface ProfileService {
    void updateLoginStreak(Profile user);
}
