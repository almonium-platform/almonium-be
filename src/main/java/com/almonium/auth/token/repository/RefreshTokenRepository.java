package com.almonium.auth.token.repository;

import com.almonium.auth.token.model.entity.RefreshToken;
import com.almonium.user.core.model.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    void deleteAllByUser(User user);
}
